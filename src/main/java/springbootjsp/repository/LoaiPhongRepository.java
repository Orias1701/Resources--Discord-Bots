package springbootjsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springbootjsp.model.LoaiPhong;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoaiPhongRepository extends JpaRepository<LoaiPhong, String> {

    // Tìm kiếm theo từ khóa (mã loại, tên loại, số giường)
    @Query("SELECT l FROM LoaiPhong l " +
            "WHERE (:keyword IS NULL OR l.maLoai LIKE %:keyword% " +
            "   OR l.tenLoai LIKE %:keyword% " +
            "   OR l.soGiuong LIKE %:keyword%)")
    List<LoaiPhong> searchByKeyword(String keyword);

    // Lọc theo khoảng giá
    @Query("SELECT l FROM LoaiPhong l " +
            "WHERE (:minPrice IS NULL OR l.giaLoai >= :minPrice) " +
            "AND (:maxPrice IS NULL OR l.giaLoai <= :maxPrice)")
    List<LoaiPhong> filterByPrice(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT MAX(n.maLoai) FROM LoaiPhong n")
    String findMaxMaLoaiPhong();
}
