package springbootjsp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import springbootjsp.model.SuDungDichVu;
import springbootjsp.model.ThanhToan;
import springbootjsp.repository.HoaDonChiTietRepository;
import springbootjsp.repository.SuDungDichVuRepository;

@Service
public class SuDungDichVuService {

    private final SuDungDichVuRepository repository;
    // private final HoaDonChiTietRepository hdctRepository; // có thể dùng nếu muốn recalc HDCT

    public SuDungDichVuService(SuDungDichVuRepository repository,
                               HoaDonChiTietRepository hdctRepository) {
        this.repository = repository;
        // this.hdctRepository = hdctRepository;
    }

    public List<SuDungDichVu> getAll() {
        return repository.findAll();
    }

    public Optional<SuDungDichVu> getById(String maSDDV) {
        return repository.findById(maSDDV);
    }

    private void assertSddvMutableForMoney(SuDungDichVu sddv) {
        if (sddv != null && sddv.getThanhToan() == ThanhToan.DA_THANH_TOAN) {
            throw new IllegalStateException("SDDV đã thanh toán - không được cập nhật tiền.");
        }
    }

    public SuDungDichVu save(SuDungDichVu sddv) {
        if (sddv == null) throw new IllegalArgumentException("SuDungDichVu không được null");

        final String ma = sddv.getMaSDDV();
        final boolean isCreate = (ma == null || ma.isEmpty()) || !repository.existsById(ma);

        if (isCreate) {
            if (ma == null || ma.isEmpty()) {
                sddv.setMaSDDV(generateNextMaSDDV());
            }
            if (sddv.getTongTien() == null) sddv.setTongTien(BigDecimal.ZERO);
            if (sddv.getThanhToan() == null) sddv.setThanhToan(ThanhToan.CHUA_THANH_TOAN);
        } else {
            // Load bản hiện tại để kiểm tra trạng thái
            @SuppressWarnings("null")
            SuDungDichVu cur = repository.findById(ma)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy SDDV: " + ma));

            // chặn sửa tiền nếu đã thanh toán
            assertSddvMutableForMoney(cur);

            // nếu cho phép set trực tiếp tongTien qua save(...), kiểm tra hợp lệ:
            BigDecimal tt = sddv.getTongTien();
            if (tt != null && tt.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Tổng tiền không hợp lệ (âm).");
            }
        }
        return repository.save(sddv);
    }

    public void deleteById(String maSDDV) {
        repository.findById(maSDDV).ifPresent(s -> {
            assertSddvMutableForMoney(s); // không cho xoá nếu đã thanh toán
            repository.deleteById(maSDDV);
        });
    }

    public String generateNextMaSDDV() {
        String maxMa = repository.findAll()
                .stream()
                .map(SuDungDichVu::getMaSDDV)
                .max(String::compareTo)
                .orElse("SD001");
        String numberPart = maxMa.substring(2);
        int num;
        try { num = Integer.parseInt(numberPart); }
        catch (NumberFormatException e) { num = 0; }
        num++;
        return String.format("SD%03d", num);
    }

    // Tim kiem theo keyword
    public List<SuDungDichVu> search(String keyword) {
        return repository.searchByKeyword(keyword);
    }

    // Loc theo khoang ngay
    public List<SuDungDichVu> filterByDate(LocalDate startDate, LocalDate endDate) {
        return repository.filterByDate(startDate, endDate);
    }
    
}
