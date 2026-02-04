package springbootjsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springbootjsp.model.DichVu;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DichVuRepository extends JpaRepository<DichVu, String> {

    // Tìm kiếm theo từ khóa (mã, tên, loại dịch vụ)
    @Query("SELECT d FROM DichVu d " +
            "WHERE (:keyword IS NULL OR d.maDichVu LIKE %:keyword% " +
            "   OR d.tenDichVu LIKE %:keyword% " +
            "   OR d.loaiDichVu LIKE %:keyword%)")
    List<DichVu> searchByKeyword(String keyword);

    // Lọc theo khoảng giá
    @Query("SELECT d FROM DichVu d " +
            "WHERE (:minPrice IS NULL OR d.giaDichVu >= :minPrice) " +
            "AND (:maxPrice IS NULL OR d.giaDichVu <= :maxPrice)")
    List<DichVu> filterByPrice(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT MAX(d.maDichVu) FROM DichVu d")
    String findMaxMaDichVu();
}
