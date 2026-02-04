package springbootjsp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springbootjsp.model.DatPhong;
import springbootjsp.model.KiemTraPhong;
import springbootjsp.model.Phong;
import springbootjsp.model.ThanhToan;
import springbootjsp.repository.DatPhongRepository;
import springbootjsp.repository.HoaDonChiTietRepository;
import springbootjsp.repository.KiemTraPhongRepository;
import springbootjsp.repository.PhongRepository;

@Service
public class KiemTraPhongService {

    private final KiemTraPhongRepository repository;
    private final HoaDonChiTietRepository hdctRepository;

    // NEW: cần tra phòng từ đơn đặt phòng để đối chiếu
    private final DatPhongRepository datPhongRepository;
    private final PhongRepository phongRepository;

    public KiemTraPhongService(KiemTraPhongRepository repository,
                               HoaDonChiTietRepository hdctRepository,
                               DatPhongRepository datPhongRepository,
                               PhongRepository phongRepository) {
        this.repository = repository;
        this.hdctRepository = hdctRepository;
        this.datPhongRepository = datPhongRepository;
        this.phongRepository = phongRepository;
    }

    public List<KiemTraPhong> getAll() { return repository.findAll(); }

    public List<KiemTraPhong> search(String keyword) { return repository.searchByKeyword(keyword); }

    public List<KiemTraPhong> filterByTrangThai(KiemTraPhong.TrangThai trangThai) {
        return repository.filterByTrangThai(trangThai);
    }

    public Optional<KiemTraPhong> getById(String maKiemTra) { return repository.findById(maKiemTra); }

    private void assertKtMutable(KiemTraPhong current) {
        if (current != null && current.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            throw new IllegalStateException("Kiểm tra phòng đã ĐÃ THANH TOÁN - không được phép sửa hoặc xóa.");
        }
    }

    private void assertKtMutableById(String maKiemTra) {
        KiemTraPhong cur = repository.findById(maKiemTra)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Kiểm tra phòng: " + maKiemTra));
        assertKtMutable(cur);
    }

    // NEW: Rule kiểm tra phòng trùng với mã đặt phòng
    // NEW: Rule kiểm tra phòng trùng với mã đặt phòng
    private void assertPhongHopLeVaDongBo(KiemTraPhong in) {
        if (in == null) return;

        final String maPhongForm = (in.getPhong() != null) ? in.getPhong().getMaPhong() : null;
        final String maDatPhong  = (in.getDatPhong() != null) ? in.getDatPhong().getMaDatPhong() : null;

        if (maDatPhong == null || maDatPhong.isBlank()) {
            return; // không chọn đặt phòng thì bỏ qua rule này (tuỳ nghiệp vụ)
        }

        DatPhong dp = datPhongRepository.findById(maDatPhong)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Đặt phòng: " + maDatPhong));

        // ❗Không cho dùng nếu Đặt phòng đã thanh toán
        if (dp.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            throw new IllegalStateException("Mã đặt phòng " + maDatPhong + " đã thanh toán - không thể chọn.");
        }

        // ❗Không cho dùng nếu Đặt phòng đang đợi
        if (dp.getTinhTrang() == DatPhong.TinhTrang.DANG_DOI) {
            throw new IllegalStateException("Mã đặt phòng " + maDatPhong + " đang ở trạng thái DANG_DOI - không thể chọn.");
        }

        final Phong phongCuaDP = dp.getMaPhong();
        final String maPhongCuaDP = (phongCuaDP != null) ? phongCuaDP.getMaPhong() : null;

        if (maPhongForm == null && maPhongCuaDP != null) {
            in.setPhong(phongRepository.getReferenceById(maPhongCuaDP));
            return;
        }
        if (maPhongForm != null && maPhongCuaDP != null && !maPhongForm.equals(maPhongCuaDP)) {
            throw new IllegalStateException(
                "Phòng được chọn (" + maPhongForm + ") không khớp với phòng của Mã đặt phòng "
                + maDatPhong + " (" + maPhongCuaDP + ")."
            );
        }
    }


    /**
     * SAVE an toàn:
     * - CREATE: set default cho tienKiemTra & thanhToan nếu null
     * - UPDATE: merge có điều kiện, KHÔNG ghi đè tienKiemTra nếu client không gửi lên
     * - NEW RULE: maDatPhong & phong phải khớp (và tự gắn phòng theo đặt phòng nếu phòng trống)
     */
    @Transactional
    public KiemTraPhong save(KiemTraPhong incoming) {
        if (incoming == null) throw new IllegalArgumentException("incoming null");

        // Áp rule phòng ↔ mã đặt phòng trước khi lưu
        assertPhongHopLeVaDongBo(incoming);

        // UPDATE
        if (incoming.getMaKiemTra() != null && repository.existsById(incoming.getMaKiemTra())) {
            KiemTraPhong current = repository.findById(incoming.getMaKiemTra())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy: " + incoming.getMaKiemTra()));
            assertKtMutable(current);

            // ===== Merge có điều kiện =====
            if (incoming.getTienKiemTra() == null) {
                incoming.setTienKiemTra(current.getTienKiemTra());
            }
            if (incoming.getThanhToan() == null) {
                incoming.setThanhToan(current.getThanhToan());
            }

            // Bạn có thể merge thêm các field khác nếu cần:
            // if (incoming.getNgayKiemTra() == null) incoming.setNgayKiemTra(current.getNgayKiemTra());
            // if (incoming.getGhiChu() == null) incoming.setGhiChu(current.getGhiChu());
            // ...

            return repository.save(incoming);
        }

        // CREATE
        if (incoming.getTienKiemTra() == null) incoming.setTienKiemTra(BigDecimal.ZERO);
        if (incoming.getThanhToan() == null) incoming.setThanhToan(ThanhToan.CHUA_THANH_TOAN);

        return repository.save(incoming);
    }

    public void deleteById(String maKiemTra) {
        assertKtMutableById(maKiemTra);
        repository.deleteById(maKiemTra);
    }

    @Transactional
    public void setTrangThaiThanhToan(String maKiemTra, ThanhToan status) {
        KiemTraPhong ktp = repository.findById(maKiemTra)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Kiểm tra phòng: " + maKiemTra));
        ktp.setThanhToan(status);
        repository.save(ktp);
    }

    public String generateNextMaKiemTra() {
        String maxMa = repository.findMaxMaKiemTra();
        if (maxMa == null || maxMa.length() < 3) return "KT001";
        String numPart = maxMa.substring(2);
        int num;
        try { num = Integer.parseInt(numPart); } catch (NumberFormatException e) { num = 0; }
        num++;
        return String.format("KT%03d", num);
    }

    public List<KiemTraPhong> getDanhSachKiemTraChuaThanhToan() {
        return repository.findByThanhToan(ThanhToan.CHUA_THANH_TOAN);
    }

    public BigDecimal getTienKiemTraByMaKiemTra(String maKiemTra) {
        KiemTraPhong ktp = repository.findByMaKiemTra(maKiemTra);
        return (ktp != null) ? ktp.getTienKiemTra() : null;
    }

    public Optional<KiemTraPhong> layKiemTraPhongBangMa(String maKiemTra) {
        var list = repository.findByMaKiemTraOrderByNgayKiemTraDesc(maKiemTra);
        return list.stream().findFirst();
    }

    @Transactional
    public void capNhatTienKiemTra(String maKiemTra, BigDecimal tongTienMoi) {
        repository.findById(maKiemTra).ifPresent(ktp -> {
            if (ktp.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
                throw new IllegalStateException("Kiểm tra phòng đã ĐÃ THANH TOÁN - không được cập nhật tiền.");
            }

            ktp.setTienKiemTra(tongTienMoi);
            KiemTraPhong savedKtp = repository.save(ktp);

            hdctRepository.findByKiemTraPhong_MaKiemTra(savedKtp.getMaKiemTra())
                    .ifPresent(hdct -> {
                        hdct.setKiemTraPhong(savedKtp);
                        hdct.setTienKiemTra(savedKtp.getTienKiemTra());

                        BigDecimal sum = BigDecimal.ZERO;
                        if (hdct.getTienDatPhong() != null) sum = sum.add(hdct.getTienDatPhong());
                        if (hdct.getTienKiemTra() != null) sum = sum.add(hdct.getTienKiemTra());
                        if (hdct.getTienSddv() != null) sum = sum.add(hdct.getTienSddv());
                        hdct.setThanhTien(sum);

                        hdctRepository.save(hdct);
                    });
        });
    }
    
}
