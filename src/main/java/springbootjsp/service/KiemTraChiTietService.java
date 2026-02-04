package springbootjsp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import springbootjsp.model.KiemTraChiTiet;
import springbootjsp.model.KiemTraPhong;
import springbootjsp.model.ThanhToan;
import springbootjsp.model.ThietBi;
import springbootjsp.model.ThietBiPhong;
import springbootjsp.repository.KiemTraChiTietRepository;
import springbootjsp.repository.KiemTraPhongRepository;
import springbootjsp.repository.ThietBiPhongRepository;
import springbootjsp.repository.ThietBiRepository;

@Service
public class KiemTraChiTietService {

    @Autowired private KiemTraChiTietRepository kiemTraChiTietRepository;
    @Autowired private KiemTraPhongRepository kiemTraPhongRepository;
    @Autowired private ThietBiRepository thietBiRepository;
    @Autowired private ThietBiPhongRepository thietBiPhongRepository;
    // @Autowired private PhongRepository phongRepository;
    @Autowired private KiemTraPhongService kiemTraPhongService;

    public List<KiemTraPhong> layTatCaKiemTraPhong() {
        return kiemTraPhongRepository.findAll();
    }

    public Optional<KiemTraPhong> layKiemTraPhongBangMa(String maKiemTra) {
        return kiemTraPhongRepository.findById(maKiemTra);
    }

    public List<String> layMaThietBiTheoMaPhong(String maPhong) {
        List<ThietBiPhong> thietBiPhongs = thietBiPhongRepository.findByPhong_MaPhong(maPhong);
        return thietBiPhongs.stream()
                .map(thietBiPhong -> thietBiPhong.getThietBi().getMaThietBi())
                .collect(Collectors.toList());
    }

    public BigDecimal layDenBuTheoMaThietBi(String maThietBi) {
        Optional<ThietBi> thietBiOpt = thietBiRepository.findById(maThietBi);
        return thietBiOpt.map(ThietBi::getDenBu).orElse(BigDecimal.ZERO);
    }

    // ===== GUARD: KHÓA CHỈNH SỬA CHI TIẾT NẾU KIỂM TRA PHÒNG ĐÃ THANH TOÁN =====
    private void assertKtEditable(String maKiemTra) {
        if (maKiemTra == null || maKiemTra.isBlank()) return;
        KiemTraPhong kt = kiemTraPhongRepository.findById(maKiemTra)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Kiểm tra phòng: " + maKiemTra));
        if (kt.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            throw new IllegalStateException("Kiểm tra phòng đã ĐÃ THANH TOÁN - không được thêm/sửa/xóa chi tiết.");
        }
    }
    // ==========================================================================

    public void luuKiemTraChiTiet(String maKiemTra, String maThietBi, String tinhTrang, int soLuongHong, String ghiChu) {
        // KHÓA: không cho thêm nếu KT đã ĐÃ_THANH_TOÁN
        assertKtEditable(maKiemTra);

        Optional<KiemTraPhong> kiemTraPhongOpt = kiemTraPhongRepository.findById(maKiemTra);
        Optional<ThietBi> thietBiOpt = thietBiRepository.findById(maThietBi);
        List<ThietBiPhong> thietBiPhongOpt = thietBiPhongRepository.findByThietBi_MaThietBi(maThietBi);
        if (kiemTraPhongOpt.isPresent() && thietBiOpt.isPresent()) {
            KiemTraPhong kiemTraPhong = kiemTraPhongOpt.get();
            ThietBi thietBi = thietBiOpt.get();
            ThietBiPhong thietBiPhong = thietBiPhongOpt.get(0);
            if (soLuongHong > thietBiPhong.getSoLuong()) {
                throw new IllegalArgumentException("Số lượng hỏng vượt quá số lượng thiết bị trong phòng!");
            }

            KiemTraChiTiet.KiemTraChiTietId id = new KiemTraChiTiet.KiemTraChiTietId(maKiemTra, maThietBi);

            KiemTraChiTiet chiTiet = new KiemTraChiTiet();
            chiTiet.setId(id);
            chiTiet.setKiemTraPhong(kiemTraPhong);
            chiTiet.setMaPhong(kiemTraPhong.getPhong().getMaPhong());
            chiTiet.setMaThietBi(maThietBi);
            chiTiet.setTinhTrang(KiemTraChiTiet.TinhTrang.valueOf(tinhTrang));
            chiTiet.setSoLuongHong(soLuongHong);
            chiTiet.setGhiChu(ghiChu);

            if (tinhTrang.equals("HONG")) {
                BigDecimal tongDenBu = thietBi.getDenBu().multiply(BigDecimal.valueOf(soLuongHong));
                chiTiet.setDenBu(tongDenBu);
            } else {
                chiTiet.setDenBu(BigDecimal.ZERO);
            }

            kiemTraChiTietRepository.save(chiTiet);
            syncSauKhiCapNhatChiTiet(maKiemTra);
        }
    }

    public Optional<KiemTraChiTiet> getById(KiemTraChiTiet.KiemTraChiTietId id) {
        return kiemTraChiTietRepository.findById(id);
    }

    public void suaKiemTraChiTiet(KiemTraChiTiet.KiemTraChiTietId id, String tinhTrang, int soLuongHong, String ghiChu) {
        // KHÓA: không cho sửa nếu KT đã ĐÃ_THANH_TOÁN
        assertKtEditable(id.getMaKiemTra());

        Optional<KiemTraChiTiet> existingRecord = kiemTraChiTietRepository.findById(id);

        if (existingRecord.isPresent()) {
            KiemTraChiTiet recordToUpdate = existingRecord.get();

            List<ThietBiPhong> thietBiPhongList = thietBiPhongRepository.findByThietBi_MaThietBi(
                    recordToUpdate.getMaThietBi()
            );
            Optional<ThietBiPhong> thietBiPhongOpt = thietBiPhongList.isEmpty() ? Optional.empty() : Optional.of(thietBiPhongList.get(0));

            if (thietBiPhongOpt.isPresent()) {
                ThietBiPhong thietBiPhong = thietBiPhongOpt.get();
                if (soLuongHong > thietBiPhong.getSoLuong()) {
                    throw new IllegalArgumentException("Số lượng hỏng (" + soLuongHong + ") vượt quá số lượng thiết bị hiện có trong phòng (" + thietBiPhong.getSoLuong() + ")!");
                }
            } else {
                throw new IllegalArgumentException("Không tìm thấy thông tin thiết bị trong phòng.");
            }

            recordToUpdate.setTinhTrang(KiemTraChiTiet.TinhTrang.valueOf(tinhTrang));
            recordToUpdate.setSoLuongHong(soLuongHong);
            recordToUpdate.setGhiChu(ghiChu);

            if (tinhTrang.equals("HONG")) {
                Optional<ThietBi> thietBiOpt = thietBiRepository.findById(recordToUpdate.getMaThietBi());
                thietBiOpt.ifPresent(thietBi -> {
                    BigDecimal tongDenBu = thietBi.getDenBu().multiply(BigDecimal.valueOf(soLuongHong));
                    recordToUpdate.setDenBu(tongDenBu);
                });
            } else {
                recordToUpdate.setDenBu(BigDecimal.ZERO);
            }

            kiemTraChiTietRepository.save(recordToUpdate);
            syncSauKhiCapNhatChiTiet(id.getMaKiemTra());
        } else {
            throw new IllegalArgumentException("Không tìm thấy chi tiết kiểm tra để cập nhật.");
        }
    }

    public void xoaKiemTraChiTiet(KiemTraChiTiet.KiemTraChiTietId id) {
        // KHÓA: không cho xoá nếu KT đã ĐÃ_THANH_TOÁN
        assertKtEditable(id.getMaKiemTra());

        String maKiemTra = kiemTraChiTietRepository.findById(id)
                .map(ct -> ct.getKiemTraPhong() == null ? null : ct.getKiemTraPhong().getMaKiemTra())
                .orElse(null);

        kiemTraChiTietRepository.deleteById(id);
        syncSauKhiCapNhatChiTiet(maKiemTra);
    }

    public List<KiemTraChiTiet> layTatCaKiemTraChiTiet() {
        return kiemTraChiTietRepository.findAll();
    }

    public List<KiemTraChiTiet> timKiemTheoTuKhoa(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return kiemTraChiTietRepository.searchByKeyword(keyword);
        }
        return kiemTraChiTietRepository.findAll();
    }

    public List<KiemTraChiTiet> locTheoTinhTrang(String tinhTrang) {
        if (tinhTrang != null && !tinhTrang.trim().isEmpty()) {
            KiemTraChiTiet.TinhTrang enumTinhTrang = KiemTraChiTiet.TinhTrang.valueOf(tinhTrang.toUpperCase());
            return kiemTraChiTietRepository.filterByTinhTrang(enumTinhTrang);
        }
        return kiemTraChiTietRepository.findAll();
    }

    // ====== ĐỒNG BỘ SAU KHI CHI TIẾT THAY ĐỔI ======
    private BigDecimal tinhTongDenBuTheoMaKiemTra(String maKiemTra) {
        return kiemTraChiTietRepository.findAll()
                .stream()
                .filter(ct -> ct.getKiemTraPhong() != null
                        && ct.getKiemTraPhong().getMaKiemTra() != null
                        && ct.getKiemTraPhong().getMaKiemTra().equals(maKiemTra))
                .map(ct -> ct.getDenBu() == null ? BigDecimal.ZERO : ct.getDenBu())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    private void syncSauKhiCapNhatChiTiet(String maKiemTra) {
        if (maKiemTra == null || maKiemTra.isBlank()) return;
        BigDecimal tongDenBu = tinhTongDenBuTheoMaKiemTra(maKiemTra);
        kiemTraPhongService.capNhatTienKiemTra(maKiemTra, tongDenBu);
    }
}
