package springbootjsp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springbootjsp.model.ChiTietSuDungDichVu;
import springbootjsp.model.DichVu;
import springbootjsp.model.SuDungDichVu;
import springbootjsp.model.ThanhToan;
import springbootjsp.repository.ChiTietSuDungDichVuRepository;
import springbootjsp.repository.DichVuRepository;
import springbootjsp.repository.HoaDonChiTietRepository;
import springbootjsp.repository.SuDungDichVuRepository;

@Service
@Transactional
public class ChiTietSuDungDichVuService {

    private final ChiTietSuDungDichVuRepository repository;
    private final DichVuRepository dichVuRepository;
    private final SuDungDichVuRepository suDungDichVuRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;

    public ChiTietSuDungDichVuService(ChiTietSuDungDichVuRepository repository,
                                      DichVuRepository dichVuRepository,
                                      SuDungDichVuRepository suDungDichVuRepository,
                                      HoaDonChiTietRepository hoaDonChiTietRepository) {
        this.repository = repository;
        this.dichVuRepository = dichVuRepository;
        this.suDungDichVuRepository = suDungDichVuRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
    }

    public List<ChiTietSuDungDichVu> getAll() {
        return repository.findAll();
    }

    public Optional<ChiTietSuDungDichVu> getById(Long id) {
        return repository.findById(id);
    }

    private void assertSddvMutableForMoney(SuDungDichVu sddv) {
        if (sddv != null && sddv.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            throw new IllegalStateException("SDDV đã thanh toán - không được cập nhật tiền.");
        }
    }

    public ChiTietSuDungDichVu save(ChiTietSuDungDichVu ct) {
        if (ct == null) throw new IllegalArgumentException("Chi tiết không được null");
        if (ct.getSoLuong() == null || ct.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng phải > 0");
        }
        if (ct.getDichVu() == null || ct.getDichVu().getMaDichVu() == null) {
            throw new IllegalArgumentException("Thiếu mã dịch vụ");
        }
        if (ct.getSuDungDichVu() == null || ct.getSuDungDichVu().getMaSDDV() == null) {
            throw new IllegalArgumentException("Thiếu mã sử dụng dịch vụ");
        }

        final boolean isNew = (ct.getId() == null);

        // Lấy SDDV cũ nếu là sửa (để recalc nếu CT chuyển SDDV)
        String oldMaSDDV = null;
        if (!isNew) {
            ChiTietSuDungDichVu cur = repository.findById(ct.getId())
                .orElseThrow(() -> new IllegalArgumentException("Chi tiết không tồn tại: id=" + ct.getId()));
            oldMaSDDV = (cur.getSuDungDichVu() != null) ? cur.getSuDungDichVu().getMaSDDV() : null;
        }

        // Load entity managed
        DichVu dv = dichVuRepository.findById(ct.getDichVu().getMaDichVu())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ: " + ct.getDichVu().getMaDichVu()));
        SuDungDichVu sddv = suDungDichVuRepository.findById(ct.getSuDungDichVu().getMaSDDV())
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy SDDV: " + ct.getSuDungDichVu().getMaSDDV()));

        // CHẶN: nếu SDDV đã thanh toán thì không cho đụng vào tiền
        assertSddvMutableForMoney(sddv);

        ct.setDichVu(dv);
        ct.setSuDungDichVu(sddv);

        BigDecimal giaDichVu = dv.getGiaDichVu();
        if (giaDichVu == null) throw new IllegalArgumentException("Giá dịch vụ không được null");

        BigDecimal thanhTien = giaDichVu.multiply(BigDecimal.valueOf(ct.getSoLuong()));
        ct.setThanhTien(thanhTien);

        // Lưu chi tiết
        ChiTietSuDungDichVu saved = repository.save(ct);

        // Để an toàn đồng thời: luôn recalc sau khi thêm/sửa
        recalcTongTien(sddv.getMaSDDV());
        if (oldMaSDDV != null && !oldMaSDDV.equals(sddv.getMaSDDV())) {
            recalcTongTien(oldMaSDDV);
        }

        return saved;
    }

    public void delete(Long id) {
        Optional<ChiTietSuDungDichVu> opt = repository.findById(id);
        if (opt.isPresent()) {
            SuDungDichVu sddv = opt.get().getSuDungDichVu();
            assertSddvMutableForMoney(sddv); // chặn xoá nếu đã thanh toán
            String maSDDV = sddv.getMaSDDV();
            repository.deleteById(id);
            recalcTongTien(maSDDV);
        } else {
            // id không tồn tại vẫn gọi deleteById cho idempotent
            repository.deleteById(id);
        }
    }

    public void recalcTongTien(String maSDDV) {
        suDungDichVuRepository.findById(maSDDV).ifPresent(s -> {
            assertSddvMutableForMoney(s); // chặn recalc nếu SDDV đã thanh toán

            BigDecimal tong = repository.sumThanhTienByMaSDDV(maSDDV);
            s.setTongTien((tong != null) ? tong : BigDecimal.ZERO);
            suDungDichVuRepository.save(s);

            // Cập nhật lại HoaDonChiTiet theo SDDV
            updateHoaDonChiTiet(s);
        });
    }

    /**
     * Cập nhật HoaDonChiTiet sau khi SuDungDichVu thay đổi.
     * Null-safe với các khoản còn lại, và chặn nếu HDCT đã thanh toán.
     */
    private void updateHoaDonChiTiet(SuDungDichVu sddv) {
        hoaDonChiTietRepository.findBySuDungDichVu_MaSDDV(sddv.getMaSDDV())
            .ifPresent(hdct -> {
                // Nếu có trạng thái thanh toán trên HDCT thì chặn
                if (hdct.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
                    throw new IllegalStateException("HDCT đã thanh toán - không được cập nhật.");
                }

                hdct.setSuDungDichVu(sddv);
                hdct.setTienSddv(sddv.getTongTien());

                BigDecimal dp = (hdct.getTienDatPhong() != null) ? hdct.getTienDatPhong() : BigDecimal.ZERO;
                BigDecimal kt = (hdct.getTienKiemTra() != null) ? hdct.getTienKiemTra() : BigDecimal.ZERO;
                BigDecimal dv = (hdct.getTienSddv() != null) ? hdct.getTienSddv() : BigDecimal.ZERO;

                hdct.setThanhTien(dp.add(kt).add(dv));

                hoaDonChiTietRepository.save(hdct);
            });
    }

    public List<ChiTietSuDungDichVu> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.searchByKeyword(keyword);
    }

    public List<ChiTietSuDungDichVu> filterBySoLuong(Integer minSL, Integer maxSL) {
        return repository.filterBySoLuong(minSL, maxSL);
    }
}
