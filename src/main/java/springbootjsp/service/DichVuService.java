package springbootjsp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import springbootjsp.model.DichVu;
import springbootjsp.repository.DichVuRepository;

@Service
public class DichVuService {

    private final DichVuRepository repository;

    public DichVuService(DichVuRepository repository) {
        this.repository = repository;
    }

    // Lấy tất cả dịch vụ
    public List<DichVu> getAll() {
        return repository.findAll();
    }

    // Tìm kiếm theo từ khóa
    public List<DichVu> search(String keyword) {
        return repository.searchByKeyword(keyword);
    }

    // Lọc theo khoảng giá
    public List<DichVu> filterByPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return repository.filterByPrice(minPrice, maxPrice);
    }

    // Lấy theo mã
    public Optional<DichVu> getById(String maDichVu) {
        return repository.findById(maDichVu);
    }

    // Thêm hoặc cập nhật
    public DichVu save(DichVu dv) {
        return repository.save(dv);
    }

    // Xóa theo mã
    public void deleteById(String maDichVu) {
        repository.deleteById(maDichVu);
    }

    // Sinh mã dịch vụ mới
    public String generateNextMaDichVu() {
        String maxMa = repository.findMaxMaDichVu();

        if (maxMa == null || maxMa.length() < 3) {
            return "DV001";
        }

        String numberPart = maxMa.substring(2); // DV001 -> 001
        int num = 0;

        try {
            num = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            num = 0;
        }

        num++;
        return String.format("DV%03d", num);
    }
}
