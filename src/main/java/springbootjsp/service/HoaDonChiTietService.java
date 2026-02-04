package springbootjsp.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springbootjsp.model.HoaDonChiTiet;
import springbootjsp.model.ThanhToan;
import springbootjsp.repository.DatPhongRepository;
import springbootjsp.repository.HoaDonChiTietRepository;
import springbootjsp.repository.HoaDonRepository;
import springbootjsp.repository.KiemTraPhongRepository;
import springbootjsp.repository.SuDungDichVuRepository;

/**
 * HoaDonChiTietService
 * - TÁCH RÕ luồng: createHdct (INSERT) vs updateHdct (UPDATE)
 * - Quy ước: id == null => THÊM MỚI; id != null => CHỈNH SỬA
 * - Guard đầy đủ để tránh "sửa" bị thành "thêm dòng"
 */
@Transactional
@Service
public class HoaDonChiTietService {

    private final HoaDonChiTietRepository repository;
    private final HoaDonRepository hoaDonRepository;
    private final DatPhongRepository datPhongRepository;
    private final SuDungDichVuRepository sddvRepository;
    private final KiemTraPhongRepository ktRepository;
    private final DatPhongService datPhongService;
    private final ChiTietSuDungDichVuService ctSddvService;

    // OPTIONAL: nếu bạn cần cascade SDDV thì inject thêm service chi tiết SDDV
    // private final ChiTietSuDungDichVuService ctSddvService;

    public HoaDonChiTietService(HoaDonChiTietRepository repository,
                                HoaDonRepository hoaDonRepository,
                                DatPhongRepository datPhongRepository,
                                SuDungDichVuRepository sddvRepository,
                                KiemTraPhongRepository ktRepository,
                                DatPhongService datPhongService
                                , ChiTietSuDungDichVuService ctSddvService) {
        this.repository = repository;
        this.hoaDonRepository = hoaDonRepository;
        this.datPhongRepository = datPhongRepository;
        this.sddvRepository = sddvRepository;
        this.ktRepository = ktRepository;
        this.datPhongService = datPhongService;
        this.ctSddvService = ctSddvService;
    }

    // ===== Helpers trạng thái =====
    private boolean isOpen(HoaDonChiTiet ct) {
        // null hoặc CHUA_THANH_TOAN coi là đang mở (cho phép cập nhật tiền/cascade)
        return ct != null && (ct.getThanhToan() == null || ct.getThanhToan() == ThanhToan.CHUA_THANH_TOAN);
    }
    private boolean isFrozen(HoaDonChiTiet ct) {
        return ct != null && ct.getThanhToan() == ThanhToan.DA_THANH_TOAN;
    }
    private static BigDecimal z(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    // ===== Router save(): null => create, !null => update =====
    public HoaDonChiTiet save(HoaDonChiTiet hdct) {
        if (hdct == null) throw new IllegalArgumentException("HDCT không được null");

        // Nếu client gửi id nhưng DB không có -> báo lỗi (tránh vô tình INSERT)
        if (hdct.getId() != null && !repository.existsById(hdct.getId())) {
            throw new IllegalArgumentException("Không tìm thấy HDCT id=" + hdct.getId() + " để cập nhật");
        }

        return (hdct.getId() == null)
                ? createHdct(hdct)                // THÊM MỚI (INSERT)
                : updateHdct(hdct.getId(), hdct); // CHỈNH SỬA (UPDATE)
    }

    // ===== CREATE (INSERT) =====
    private HoaDonChiTiet createHdct(HoaDonChiTiet incoming) {
        // Bắt buộc có Hóa đơn
        assertHoaDonRequired(incoming);

        // Chặn thêm mới vào Hóa đơn đã thanh toán
        assertNotAddingToPaidInvoice(incoming);

        // Gắn (attach) các quan hệ từ ID -> entity managed (tránh transient)
        attachChildrenById(incoming);

        // Tính tiền nếu chưa đóng băng
        if (!isFrozen(incoming)) {
            getTongTienAll(incoming);
        }

        // KHÔNG trùng mã ĐP/SDDV/KT trong cùng 1 Hóa đơn (excludeId = null vì tạo mới)
        assertUniqueChildPerInvoice(incoming);

        // (Tuỳ chọn nếu còn dùng rule cũ)
        // assertNoAddOrEditWhenPaidSiblingExists(incoming);
        // assertNoInvalidDuplicate(incoming);

        // LƯU: INSERT
        HoaDonChiTiet saved = repository.save(incoming);

        // Cascade nếu mở
        cascadeTotalsIfOpen(saved);

        // Đồng bộ trạng thái con (nếu có set sẵn)
        if (saved.getThanhToan() != null) {
            syncChildrenOfHdct(saved, saved.getThanhToan());
        }

        // Luôn cập nhật tổng tiền & trạng thái của Hóa đơn cha
        updateHoaDonTongTienVaTrangThai(saved.getHoaDon().getMaHoaDon());

        return saved;
    }

    // ===== UPDATE (LOAD + COPY + SAVE) =====
    private HoaDonChiTiet updateHdct(Long id, HoaDonChiTiet incoming) {
        // LUÔN load bản hiện có (managed) => đảm bảo UPDATE, tránh chèn bản mới
        HoaDonChiTiet current = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy HDCT id=" + id));

        // Nếu bản hiện tại đã ĐÃ THANH TOÁN: chỉ cho đổi trạng thái qua setHdctThanhToan(...)
        if (current.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            if (incoming.getThanhToan() != null && incoming.getThanhToan() != current.getThanhToan()) {
                setHdctThanhToan(id, incoming.getThanhToan());
                return repository.findById(id).orElse(current);
            }
            throw new IllegalStateException("HDCT đã ĐÃ THANH TOÁN, không được phép sửa nội dung.");
        }

        // Không cho đổi Hóa đơn đích khi update (tránh “chuyển nhà” gây trùng)
        assertHoaDonRequired(incoming);
        String maHdCur = (current.getHoaDon() != null) ? current.getHoaDon().getMaHoaDon() : null;
        String maHdIn  = incoming.getHoaDon().getMaHoaDon();
        if (maHdCur != null && !maHdCur.equals(maHdIn)) {
            throw new IllegalStateException("Không được đổi mã Hóa đơn của HDCT khi cập nhật.");
        }

        // ---- Copy các field được phép sửa ----
        // Nếu muốn ép đổi trạng thái chỉ đi qua setHdctThanhToan(...), bỏ dòng dưới
        current.setThanhToan(incoming.getThanhToan());

        // Cho phép đổi child (Đặt phòng / SDDV / Kiểm tra)
        current.setDatPhong(incoming.getDatPhong());
        current.setSuDungDichVu(incoming.getSuDungDichVu());
        current.setKiemTraPhong(incoming.getKiemTraPhong());
        attachChildrenById(current);

        // Tính lại tiền nếu chưa đóng băng
        if (!isFrozen(current)) {
            getTongTienAll(current);
        }

        // KHÔNG trùng child trong cùng 1 Hóa đơn (excludeId = current.getId())
        assertUniqueChildPerInvoice(current);

        // (Tuỳ chọn nếu còn dùng rule cũ)
        // assertNoAddOrEditWhenPaidSiblingExists(current);
        // assertNoInvalidDuplicate(current);

        // SAVE UPDATE
        HoaDonChiTiet saved = repository.save(current);

        // Cascade nếu mở
        cascadeTotalsIfOpen(saved);

        // Đồng bộ trạng thái con (nếu có set)
        if (saved.getThanhToan() != null) {
            syncChildrenOfHdct(saved, saved.getThanhToan());
        }

        // Cập nhật tổng tiền & trạng thái Hóa đơn
        updateHoaDonTongTienVaTrangThai(saved.getHoaDon().getMaHoaDon());

        return saved;
    }

    // ===== Public APIs khác =====
    public List<HoaDonChiTiet> getAll() {
        return repository.findAll();
    }

    public Optional<HoaDonChiTiet> getById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        HoaDonChiTiet ct = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy HDCT id=" + id));

        // Chặn xóa nếu đã thanh toán
        if (ct.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            throw new IllegalStateException("HDCT đã ĐÃ THANH TOÁN, không được phép xóa.");
        }

        String maHoaDon = (ct.getHoaDon() != null) ? ct.getHoaDon().getMaHoaDon() : null;

        repository.deleteById(id);

        if (maHoaDon != null) {
            updateHoaDonTongTienVaTrangThai(maHoaDon);
        }
    }

    // ===== Tính tiền HDCT từ các nguồn =====
    public void getTongTienAll(HoaDonChiTiet hdct) {
        BigDecimal tienDatPhong = (hdct.getDatPhong() != null)
                ? z(hdct.getDatPhong().getTongTien())
                : BigDecimal.ZERO;

        BigDecimal tienKiemTra = (hdct.getKiemTraPhong() != null)
                ? z(hdct.getKiemTraPhong().getTienKiemTra())
                : BigDecimal.ZERO;

        BigDecimal tienSddv = (hdct.getSuDungDichVu() != null)
                ? z(hdct.getSuDungDichVu().getTongTien())
                : BigDecimal.ZERO;

        BigDecimal thanhTien = tienDatPhong.add(tienKiemTra).add(tienSddv);

        hdct.setTienDatPhong(tienDatPhong.setScale(2, RoundingMode.HALF_UP));
        hdct.setTienKiemTra(tienKiemTra.setScale(2, RoundingMode.HALF_UP));
        hdct.setTienSddv(tienSddv.setScale(2, RoundingMode.HALF_UP));
        hdct.setThanhTien(thanhTien.setScale(2, RoundingMode.HALF_UP));
    }

    // ===== Cascade & đồng bộ =====
    private void cascadeTotalsIfOpen(HoaDonChiTiet ct) {
        if (!isOpen(ct)) return;

        if (ct.getDatPhong() != null) {
            try {
                datPhongService.syncTongTien(ct.getDatPhong().getMaDatPhong());
            } catch (Exception ignore) { }
        }
        // Nếu có ctSddvService/ktService thì bật thêm ở đây
    }

    private void syncChildrenOfHdct(HoaDonChiTiet ct, ThanhToan status) {
        // DatPhong
        if (ct.getDatPhong() != null) {
            var dp = ct.getDatPhong();
            dp.setThanhToan(status);
            datPhongRepository.save(dp);

            // Nếu HDCT đã thanh toán, đánh dấu phòng "đã trả" (nếu có logic này)
            if (status == ThanhToan.DA_THANH_TOAN) {
                try {
                    datPhongService.danhDauDaTra(dp.getMaDatPhong());
                } catch (Exception ignore) { }
            }
        }

        // KiemTraPhong
        if (ct.getKiemTraPhong() != null) {
            var kt = ct.getKiemTraPhong();
            kt.setThanhToan(status);
            ktRepository.save(kt);
        }

        // SuDungDichVu
        if (ct.getSuDungDichVu() != null) {
            var dv = ct.getSuDungDichVu();
            dv.setThanhToan(status);
            sddvRepository.save(dv);
        }
    }

    // ===== Guard/Rule helpers =====

    // Gắn các quan hệ theo ID thành entity managed (tránh transient ở save)
    private void attachChildrenById(HoaDonChiTiet target) {
        // Gắn HoaDon
        if (target.getHoaDon() != null && target.getHoaDon().getMaHoaDon() != null) {
            var maHd = target.getHoaDon().getMaHoaDon();
            target.setHoaDon(hoaDonRepository.getReferenceById(maHd));
        }

        // Gắn DatPhong
        if (target.getDatPhong() != null && target.getDatPhong().getMaDatPhong() != null) {
            var maDp = target.getDatPhong().getMaDatPhong();
            target.setDatPhong(datPhongRepository.getReferenceById(maDp));
        } else {
            target.setDatPhong(null);
        }

        // Gắn SuDungDichVu
        if (target.getSuDungDichVu() != null && target.getSuDungDichVu().getMaSDDV() != null) {
            var maSddv = target.getSuDungDichVu().getMaSDDV();
            target.setSuDungDichVu(sddvRepository.getReferenceById(maSddv));
        } else {
            target.setSuDungDichVu(null);
        }

        // Gắn KiemTraPhong
        if (target.getKiemTraPhong() != null && target.getKiemTraPhong().getMaKiemTra() != null) {
            var maKt = target.getKiemTraPhong().getMaKiemTra();
            target.setKiemTraPhong(ktRepository.getReferenceById(maKt));
        } else {
            target.setKiemTraPhong(null);
        }
    }

    // Bắt buộc có HoaDon khi create/update
    private void assertHoaDonRequired(HoaDonChiTiet in) {
        if (in.getHoaDon() == null
                || in.getHoaDon().getMaHoaDon() == null
                || in.getHoaDon().getMaHoaDon().isBlank()) {
            throw new IllegalArgumentException("Thiếu mã Hóa đơn (maHoaDon) cho HDCT.");
        }
    }
    
    // KHÔNG cho thêm mới HDCT vào Hóa đơn đã thanh toán
    private void assertNotAddingToPaidInvoice(HoaDonChiTiet hdct) {
        // Chỉ chặn khi tạo mới (id == null) và có chọn Hóa đơn
        if (hdct == null || hdct.getId() != null || hdct.getHoaDon() == null) return;

        String maHoaDon = hdct.getHoaDon().getMaHoaDon();
        if (maHoaDon == null || maHoaDon.isBlank()) return;

        var hd = hoaDonRepository.findById(maHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Hóa đơn " + maHoaDon));
        if (hd.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            throw new IllegalStateException("Hóa đơn " + maHoaDon + " đã ĐÃ THANH TOÁN. Không được thêm Hóa đơn chi tiết mới.");
        }
    }

    // KHÔNG cho trùng mã ĐP / SDDV / KT trong cùng 1 Hóa đơn (kể cả khác trạng thái)
    private void assertUniqueChildPerInvoice(HoaDonChiTiet incoming) {
        if (incoming == null || incoming.getHoaDon() == null) return;

        final String maHoaDon = incoming.getHoaDon().getMaHoaDon();
        if (maHoaDon == null || maHoaDon.isBlank()) return;

        final Long excludeId = incoming.getId(); // null nếu tạo mới

        if (incoming.getDatPhong() != null && incoming.getDatPhong().getMaDatPhong() != null) {
            String maDp = incoming.getDatPhong().getMaDatPhong();
            if (repository.existsDuplicateDp(maHoaDon, maDp, excludeId)) {
                throw new IllegalStateException(
                        "Trong Hóa đơn " + maHoaDon + " đã tồn tại HDCT cho mã Đặt phòng " + maDp + "."
                );
            }
        }
        if (incoming.getSuDungDichVu() != null && incoming.getSuDungDichVu().getMaSDDV() != null) {
            String maSddv = incoming.getSuDungDichVu().getMaSDDV();
            if (repository.existsDuplicateSddv(maHoaDon, maSddv, excludeId)) {
                throw new IllegalStateException(
                        "Trong Hóa đơn " + maHoaDon + " đã tồn tại HDCT cho mã SDDV " + maSddv + "."
                );
            }
        }
        if (incoming.getKiemTraPhong() != null && incoming.getKiemTraPhong().getMaKiemTra() != null) {
            String maKt = incoming.getKiemTraPhong().getMaKiemTra();
            if (repository.existsDuplicateKt(maHoaDon, maKt, excludeId)) {
                throw new IllegalStateException(
                        "Trong Hóa đơn " + maHoaDon + " đã tồn tại HDCT cho mã Kiểm tra " + maKt + "."
                );
            }
        }
    }

    // (Tuỳ chọn) Rule cũ: Không cho thêm/sửa khi có "sibling" ĐÃ THANH TOÁN cùng trục (ĐP/SDDV/KT)
    @SuppressWarnings("unused")
    private void assertNoAddOrEditWhenPaidSiblingExists(HoaDonChiTiet incoming) {
        // Giữ lại nếu bạn cần logic “cấm sửa/thêm nếu đã có 1 dòng ĐÃ THANH TOÁN cùng mã con”
        // (Bỏ trống để tránh chồng chéo với rule unique mới)
    }

    // (Tuỳ chọn) Rule cũ: Cho phép tồn tại 2 record cùng tổ hợp nhưng 1 CHƯA + 1 ĐÃ
    @SuppressWarnings("unused")
    private void assertNoInvalidDuplicate(HoaDonChiTiet hdct) {
        // Giữ lại nếu bạn vẫn dùng chính sách 1 CHƯA + 1 ĐÃ cho cùng tổ hợp
        // (Bỏ trống để tránh mâu thuẫn với rule unique mới)
    }

    // ===== Đồng bộ trạng thái Hóa đơn cha =====
    private void updateHoaDonTongTienVaTrangThai(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.isBlank()) return;

        List<HoaDonChiTiet> items = repository.findByHoaDon_MaHoaDon(maHoaDon);

        BigDecimal sum = items.stream()
                .map(HoaDonChiTiet::getThanhTien)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        boolean allPaid = !items.isEmpty() &&
                items.stream().allMatch(i -> i.getThanhToan() == ThanhToan.DA_THANH_TOAN);

        var hd = hoaDonRepository.findById(maHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Hóa đơn " + maHoaDon));
        hd.setTongTien(sum);
        hd.setThanhToan(allPaid ? ThanhToan.DA_THANH_TOAN : ThanhToan.CHUA_THANH_TOAN);
        hoaDonRepository.save(hd);
    }

    // ===== Đổi trạng thái HDCT (nếu cần) =====
    @Transactional
    public void setHdctThanhToan(Long idHdct, ThanhToan status) {
        if (idHdct == null || status == null) {
            throw new IllegalArgumentException("idHdct/status không được null");
        }

        // 1) Tải HDCT hiện có
        HoaDonChiTiet ct = repository.findById(idHdct)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy HDCT id=" + idHdct));

        // 2) Cập nhật trạng thái HDCT (vẫn giữ các rule an toàn bạn đã đặt)
        ct.setThanhToan(status);
        assertNoAddOrEditWhenPaidSiblingExists(ct);
        assertNoInvalidDuplicate(ct);

        // 3) Lưu HDCT
        repository.save(ct);

        // 4) ĐỒNG BỘ TRẠNG THÁI XUỐNG CÁC BẢNG CON (Đặt phòng / Kiểm tra phòng / SDDV)
        //    => chính chỗ này trước đây bạn chưa gọi nên KiemTraPhong không đổi trạng thái
        syncChildrenOfHdct(ct, status);

        // 5) Cộng lại tổng tiền & trạng thái Hóa đơn cha
        if (ct.getHoaDon() != null && ct.getHoaDon().getMaHoaDon() != null) {
            updateHoaDonTongTienVaTrangThai(ct.getHoaDon().getMaHoaDon());
        }
    }
    
    // Trong HoaDonChiTietService
    public static class TotalsDto {
        private final BigDecimal tienDatPhong;
        private final BigDecimal tienSddv;
        private final BigDecimal tienKiemTra;
        private final BigDecimal thanhTien;

        public TotalsDto(BigDecimal dp, BigDecimal dv, BigDecimal kt) {
            this.tienDatPhong = z(dp).setScale(2, RoundingMode.HALF_UP);
            this.tienSddv     = z(dv).setScale(2, RoundingMode.HALF_UP);
            this.tienKiemTra  = z(kt).setScale(2, RoundingMode.HALF_UP);
            this.thanhTien    = this.tienDatPhong.add(this.tienSddv).add(this.tienKiemTra)
                                .setScale(2, RoundingMode.HALF_UP);
        }
        public BigDecimal getTienDatPhong() { return tienDatPhong; }
        public BigDecimal getTienSddv() { return tienSddv; }
        public BigDecimal getTienKiemTra() { return tienKiemTra; }
        public BigDecimal getThanhTien() { return thanhTien; }
    }

    /** Tính nhanh tiền theo các mã đã chọn (không lưu DB). */
    public TotalsDto quickPreview(String maDatPhong, String maSddv, String maKiemTra) {
        BigDecimal dp = BigDecimal.ZERO;
        BigDecimal dv = BigDecimal.ZERO;
        BigDecimal kt = BigDecimal.ZERO;

        if (maDatPhong != null && !maDatPhong.isBlank()) {
            dp = datPhongRepository.findById(maDatPhong)
                    .map(x -> z(x.getTongTien())).orElse(BigDecimal.ZERO);
        }
        if (maSddv != null && !maSddv.isBlank()) {
            dv = sddvRepository.findById(maSddv)
                    .map(x -> z(x.getTongTien())).orElse(BigDecimal.ZERO);
        }
        if (maKiemTra != null && !maKiemTra.isBlank()) {
            kt = ktRepository.findById(maKiemTra)
                    .map(x -> z(x.getTienKiemTra())).orElse(BigDecimal.ZERO);
        }
        return new TotalsDto(dp, dv, kt);
    }
    
}