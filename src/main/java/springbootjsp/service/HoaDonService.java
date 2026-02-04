package springbootjsp.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springbootjsp.model.DatPhong;
import springbootjsp.model.HoaDon;
import springbootjsp.model.HoaDonChiTiet;
import springbootjsp.model.KhachHang;
import springbootjsp.model.KhachHang.TinhTrangKhach;
import springbootjsp.model.KiemTraPhong;
import springbootjsp.model.SuDungDichVu;
import springbootjsp.model.ThanhToan;
import springbootjsp.repository.DatPhongRepository;
import springbootjsp.repository.HoaDonChiTietRepository;
import springbootjsp.repository.HoaDonRepository;
import springbootjsp.repository.KhachHangRepository;
import springbootjsp.repository.KiemTraPhongRepository;
import springbootjsp.repository.NhanVienRepository;
import springbootjsp.repository.SuDungDichVuRepository;

@Service
@Transactional
public class HoaDonService {

    private final HoaDonRepository repository;
    private final HoaDonChiTietRepository hdctRepository;
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final DatPhongRepository datPhongRepository;
    private final KiemTraPhongRepository kiemTraPhongRepository;
    private final SuDungDichVuRepository suDungDichVuRepository;
    private final HoaDonRepository hoaDonRepository;

    public HoaDonService(HoaDonRepository repository,
                         HoaDonChiTietRepository hdctRepository,
                         NhanVienRepository nhanVienRepository,
                         KhachHangRepository khachHangRepository,
                         DatPhongRepository datPhongRepository,
                         KiemTraPhongRepository kiemTraPhongRepository,
                         SuDungDichVuRepository suDungDichVuRepository,
                         HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.repository = repository;
        this.hdctRepository = hdctRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.khachHangRepository = khachHangRepository;
        this.datPhongRepository = datPhongRepository;
        this.kiemTraPhongRepository = kiemTraPhongRepository;
        this.suDungDichVuRepository = suDungDichVuRepository;
    }

    // ===== CRUD cơ bản =====
    public List<HoaDon> getAll() { return repository.findAll(); }

    public Optional<HoaDon> getById(String maHoaDon) { return repository.findById(maHoaDon); }

    public void deleteById(String maHoaDon) { repository.deleteById(maHoaDon); }

    public List<HoaDon> search(String keyword) { return repository.searchByKeyword(keyword); }

    // ===== Save: cấp mã nếu mới, gắn NV/KH chuẩn, tính tổng tiền, và nếu có trạng thái thì cascade =====
    public HoaDon save(HoaDon hd) {
        if (hd.getMaHoaDon() == null || hd.getMaHoaDon().isBlank()) {
            hd.setMaHoaDon(generateNextMaHoaDon());
            hd.setTongTien(BigDecimal.ZERO);
        }

        // attach NV/KH
        if (hd.getMaNhanVien() != null && hd.getMaNhanVien().getMaNhanVien() != null) {
            nhanVienRepository.findById(hd.getMaNhanVien().getMaNhanVien()).ifPresent(hd::setMaNhanVien);
        } else hd.setMaNhanVien(null);

        if (hd.getKhachHang() != null && hd.getKhachHang().getMaKhachHang() != null) {
            khachHangRepository.findById(hd.getKhachHang().getMaKhachHang()).ifPresent(hd::setKhachHang);
        } else hd.setKhachHang(null);

        // ✅ mặc định CHUA_THANH_TOAN nếu chưa set
        if (hd.getThanhToan() == null) {
            hd.setThanhToan(ThanhToan.CHUA_THANH_TOAN);
        }

        // tính tổng tạm
        syncTongTien(hd);

        // chặn trùng chỉ khi đang CHUA_THANH_TOAN
        if (hd.getThanhToan() == ThanhToan.CHUA_THANH_TOAN) {
            assertOnlyOneUnpaidPerCustomer(hd);
        }

        // lưu trước
        hd = repository.save(hd);

        // nếu có trạng thái thì cascade -> recalc
        if (hd.getThanhToan() != null) {
            cascadeStatusFromHoaDon(hd.getMaHoaDon(), hd.getThanhToan());
            recalcHoaDonFromChildren(hd.getMaHoaDon());
        }
        return hd;
    }

    // ===== Tạo mã mới: HD001, HD002, ... =====
    public String generateNextMaHoaDon() {
        String maxMa = repository.findAll().stream()
                .map(HoaDon::getMaHoaDon)
                .filter(s -> s != null && s.startsWith("HD"))
                .max(String::compareTo)
                .orElse("HD000");
        int num = 0;
        try { num = Integer.parseInt(maxMa.substring(2)); } catch (Exception ignored) {}
        num++;
        return String.format("HD%03d", num);
    }

    // ===== Tính tổng tiền từ các HDCT =====
    public void syncTongTien(HoaDon hd) {
        if (hd == null || hd.getMaHoaDon() == null) {
            if (hd != null) hd.setTongTien(BigDecimal.ZERO);
            return;
        }
        List<HoaDonChiTiet> listHdct = hdctRepository.findAllByHoaDonMa(hd.getMaHoaDon());
        BigDecimal sum = BigDecimal.ZERO;
        for (HoaDonChiTiet hdct : listHdct) {
            if (hdct.getThanhTien() != null) {
                sum = sum.add(hdct.getThanhTien());
            }
        }
        hd.setTongTien(sum);
    }

    // ===== Đặt hóa đơn sang ĐÃ_THANH_TOÁN =====
    public void markHoaDonPaid(String maHoaDon) {
        HoaDon hd = repository.findById(maHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn: " + maHoaDon));

        hd.setThanhToan(ThanhToan.DA_THANH_TOAN);
        repository.save(hd);

        cascadeStatusFromHoaDon(maHoaDon, ThanhToan.DA_THANH_TOAN);
        recalcHoaDonFromChildren(maHoaDon); // giữ DA_THANH_TOAN nếu không có HDCT
    }

    // ===== Cascade trạng thái từ Hóa đơn xuống: HDCT -> DatPhong/KiemTraPhong/SuDungDichVu =====
    private void cascadeStatusFromHoaDon(String maHoaDon, ThanhToan status) {
        if (maHoaDon == null || status == null) return;

        List<HoaDonChiTiet> items = hdctRepository.findByHoaDon_MaHoaDon(maHoaDon);
        for (HoaDonChiTiet item : items) {
            // set trạng thái cho HDCT
            item.setThanhToan(status);
            hdctRepository.save(item);

            // set xuống các bản ghi con nếu có
            if (item.getDatPhong() != null) {
                DatPhong dp = item.getDatPhong();
                dp.setThanhToan(status);
                datPhongRepository.save(dp);
            }
            if (item.getKiemTraPhong() != null) {
                KiemTraPhong kt = item.getKiemTraPhong();
                kt.setThanhToan(status);
                kiemTraPhongRepository.save(kt);
            }
            if (item.getSuDungDichVu() != null) {
                SuDungDichVu dv = item.getSuDungDichVu();
                dv.setThanhToan(status);
                suDungDichVuRepository.save(dv);
            }
        }
    }

    // ===== Giới hạn mỗi KH chỉ có 1 hóa đơn CHƯA_THANH_TOÁN =====
    private void assertOnlyOneUnpaidPerCustomer(HoaDon hd) {
        if (hd == null || hd.getKhachHang() == null || hd.getKhachHang().getMaKhachHang() == null) return;
        if (hd.getThanhToan() != ThanhToan.CHUA_THANH_TOAN) return;

        String maKH = hd.getKhachHang().getMaKhachHang();
        String excludeId = (hd.getMaHoaDon() == null ? "" : hd.getMaHoaDon());

        boolean existed = repository.existsByKhachHang_MaKhachHangAndThanhToanAndMaHoaDonNot(
                maKH, ThanhToan.CHUA_THANH_TOAN, excludeId);

        if (existed) {
            throw new IllegalStateException(
                "Khách hàng " + maKH + " đã có một hóa đơn CHƯA_THANH_TOÁN khác. " +
                "Vui lòng tất toán hoặc đổi khách hàng trước khi tạo mới."
            );
        }
    }

    // ===== Tính lại trạng thái Hóa đơn dựa vào toàn bộ HDCT =====
    private void recalcHoaDonFromChildren(String maHoaDon) {
        if (maHoaDon == null) return;

        List<HoaDonChiTiet> items = hdctRepository.findByHoaDon_MaHoaDon(maHoaDon);

        BigDecimal tong = hdctRepository.sumThanhTienByMaHoaDon(maHoaDon);
        if (tong == null) tong = BigDecimal.ZERO;

        HoaDon hd = repository.findById(maHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Hóa đơn " + maHoaDon));

        ThanhToan newStatus;
        if (items.isEmpty()) {
            // ✅ Không có HDCT: GIỮ NGUYÊN trạng thái hiện tại (vd: vừa được set DA_THANH_TOAN)
            newStatus = hd.getThanhToan();
        } else {
            boolean allPaid = items.stream()
                    .allMatch(i -> i.getThanhToan() == ThanhToan.DA_THANH_TOAN);
            newStatus = allPaid ? ThanhToan.DA_THANH_TOAN : ThanhToan.CHUA_THANH_TOAN;
        }

        hd.setThanhToan(newStatus);
        hd.setTongTien(tong.setScale(2, RoundingMode.HALF_UP));

        // chỉ chặn trùng khi kết quả là CHƯA_THANH_TOÁN
        if (newStatus == ThanhToan.CHUA_THANH_TOAN) {
            assertOnlyOneUnpaidPerCustomer(hd);
        }

        repository.save(hd);
    }

    // ===== Cập nhật tình trạng khách theo tình trạng hóa đơn của khách =====
    private void updateKhachHangStatusForHoaDon(HoaDon hd) {
        if (hd == null) return;
        KhachHang kh = hd.getKhachHang();
        if (kh == null) return;
        String maKH = kh.getMaKhachHang();
        if (maKH == null || maKH.isBlank()) return;

        boolean anyUnpaid = hoaDonRepository
                .existsByKhachHang_MaKhachHangAndThanhToan(maKH, ThanhToan.CHUA_THANH_TOAN);

        boolean hasAnyInvoice = hoaDonRepository.existsByKhachHang_MaKhachHang(maKH);

        TinhTrangKhach cur = kh.getTinhTrangKhach();
        TinhTrangKhach target;

        if (anyUnpaid) {
            if (cur == TinhTrangKhach.TRONG || cur == TinhTrangKhach.DA_DAT) {
                target = cur;
            } else {
                target = TinhTrangKhach.DANG_O;
            }
        } else if (hasAnyInvoice) {
            target = TinhTrangKhach.DA_ROI;
        } else {
            return;
        }

        if (cur != target) {
            kh.setTinhTrangKhach(target);
            khachHangRepository.save(kh);
        }
    }

    // ===== DTO Option cơ bản =====
    public static final class Option {
        private final String value;
        private final String label;
        public Option(String value, String label) { this.value = value; this.label = label; }
        public String getValue() { return value; }
        public String getLabel() { return label; }
    }

    /** Option Đặt phòng có kèm mã KiểmTraPhòng tương ứng (nếu có & CHƯA_THANH_TOÁN) */
    public static final class DatPhongKtOption {
        private final String maDatPhong;
        private final String moTa;        // label hiển thị
        private final String maKiemTra;   // có thể null nếu chưa có KT tương ứng
        public DatPhongKtOption(String maDatPhong, String moTa, String maKiemTra) {
            this.maDatPhong = maDatPhong;
            this.moTa = moTa;
            this.maKiemTra = maKiemTra;
        }
        public String getMaDatPhong() { return maDatPhong; }
        public String getMoTa() { return moTa; }
        public String getMaKiemTra() { return maKiemTra; }
    }

    /** Gói dữ liệu cho form “Thêm HDCT theo Hóa đơn …” */
    public static final class EligibleChildren {
        private final String maHoaDon;
        private final String maKhachHang;
        private final List<DatPhongKtOption> datPhongOptions;
        private final List<Option> sddvOptions;

        public EligibleChildren(String maHoaDon, String maKhachHang,
                                List<DatPhongKtOption> datPhongOptions,
                                List<Option> sddvOptions) {
            this.maHoaDon = maHoaDon;
            this.maKhachHang = maKhachHang;
            this.datPhongOptions = datPhongOptions;
            this.sddvOptions = sddvOptions;
        }
        public String getMaHoaDon() { return maHoaDon; }
        public String getMaKhachHang() { return maKhachHang; }
        public List<DatPhongKtOption> getDatPhongOptions() { return datPhongOptions; }
        public List<Option> getSddvOptions() { return sddvOptions; }
    }

    /**
     * Lấy danh sách con hợp lệ cho form HDCT theo một mã Hóa đơn:
     * - Chỉ lấy các ĐP/SDDV của đúng Khách hàng gắn với Hóa đơn đó
     * - Chỉ lấy những dòng CHƯA_THANH_TOÁN
     * - Ẩn Đặt phòng có Tình trạng = DANG_DOI
     * - Với mỗi ĐP, gắn luôn mã KiểmTraPhòng (nếu có, và CHƯA_THANH_TOÁN)
     * - Nếu Hóa đơn đã DA_THANH_TOAN -> trả list rỗng
     * - Loại bỏ các ĐP/SDDV/KT đã được dùng ở bất kỳ HDCT nào
     *
     * NOTE:
     *  - Nếu bạn muốn cho phép chọn lại tài nguyên đã dùng nhưng trong CHÍNH hóa đơn hiện tại,
     *    hãy dùng phiên bản repository: existsBy...AndHoaDon_MaHoaDonNot(...)
     *    thay cho existsBy...() thuần.
     */
    @Transactional(readOnly = true)
    public EligibleChildren getEligibleChildrenForInvoice(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.isBlank()) {
            throw new IllegalArgumentException("Thiếu maHoaDon");
        }

        HoaDon hd = repository.findById(maHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Hóa đơn: " + maHoaDon));

        // ⛔ Hóa đơn đã thanh toán -> không cho thêm con
        if (hd.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            String maKHPaid = (hd.getKhachHang() != null) ? hd.getKhachHang().getMaKhachHang() : null;
            return new EligibleChildren(maHoaDon, maKHPaid, List.of(), List.of());
        }

        String maKH = (hd.getKhachHang() != null) ? hd.getKhachHang().getMaKhachHang() : null;
        if (maKH == null || maKH.isBlank()) {
            // Không có khách -> trả về list rỗng; form có thể disable các combobox
            return new EligibleChildren(maHoaDon, null, List.of(), List.of());
        }

        // ===== Đặt phòng của KH đang CHƯA_THANH_TOÁN (và không phải DANG_DOI) =====
        List<DatPhong> dps = datPhongRepository
                .findByMaKhachHang_MaKhachHangAndThanhToan(maKH, ThanhToan.CHUA_THANH_TOAN);

        List<DatPhongKtOption> dpOptions = new java.util.ArrayList<>();
        for (DatPhong dp : dps) {
            // Ẩn ĐP có tình trạng DANG_DOI
            if (dp.getTinhTrang() == DatPhong.TinhTrang.DANG_DOI) continue;

            // ❌ bỏ ĐP đã được dùng ở bất kỳ HDCT nào
            if (hdctRepository.existsByDatPhong_MaDatPhong(dp.getMaDatPhong())) continue;

            // Tìm “mã KT” đi kèm ĐP (nếu có, và CHƯA_THANH_TOÁN) — lấy mới nhất
            String maKt = kiemTraPhongRepository
                    .findFirstByDatPhong_MaDatPhongAndThanhToanOrderByNgayKiemTraDesc(
                            dp.getMaDatPhong(), ThanhToan.CHUA_THANH_TOAN
                    )
                    .map(KiemTraPhong::getMaKiemTra)
                    .orElse(null);

            // (tuỳ rule) nếu KT đã được dùng ở HDCT nào đó thì loại
            if (maKt != null && hdctRepository.existsByKiemTraPhong_MaKiemTra(maKt)) {
                maKt = null; // hoặc continue; tuỳ business
            }

            String label = dp.getMaDatPhong();
            if (dp.getMaPhong() != null) {
                label += " - Phòng " + dp.getMaPhong().getMaPhong();
            }
            dpOptions.add(new DatPhongKtOption(dp.getMaDatPhong(), label, maKt));
        }

        // ===== SDDV của KH đang CHƯA_THANH_TOÁN =====
        List<SuDungDichVu> sddvs = suDungDichVuRepository
                .findByKhachHang_MaKhachHangAndThanhToan(maKH, ThanhToan.CHUA_THANH_TOAN);

        // ❌ bỏ SDDV đã được dùng ở bất kỳ HDCT nào
        List<Option> sddvOptions = sddvs.stream()
                .filter(dv -> !hdctRepository.existsBySuDungDichVu_MaSDDV(dv.getMaSDDV()))
                .map(dv -> new Option(dv.getMaSDDV(), dv.getMaSDDV()))
                .toList();

        return new EligibleChildren(maHoaDon, maKH, dpOptions, sddvOptions);
    }


    public Map<String, Object> getHoaDonVaChiTiet(String maHoaDon) {
        System.out.println(">>> Gọi getHoaDonVaChiTiet: " + maHoaDon);
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        List<HoaDonChiTiet> chiTietList = hdctRepository.findByHoaDon_MaHoaDon(maHoaDon);
        System.out.println(">>> Số chi tiết: " + chiTietList.size());

        BigDecimal tongTien = chiTietList.stream()
                .map(HoaDonChiTiet::getThanhTien)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        hoaDon.setTongTien(tongTien);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("hoaDon", hoaDon);
        result.put("chiTiet", chiTietList);

        System.out.println(">>> Kết quả gửi về: " + result);
        return result;
    }

}
