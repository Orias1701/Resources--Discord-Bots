package springbootjsp.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springbootjsp.model.ChiTietSuDungDichVu;

@Repository
// SỬA: Thay đổi kiểu của khóa chính từ ChiTietSuDungDichVuId thành Long
public interface ChiTietSuDungDichVuRepository extends JpaRepository<ChiTietSuDungDichVu, Long> {

    @Query("SELECT c FROM ChiTietSuDungDichVu c " +
            "WHERE (:keyword IS NULL OR " +
            "CAST(c.id AS string) LIKE %:keyword% OR " +
            "c.suDungDichVu.maSDDV LIKE %:keyword% OR " +
            "c.dichVu.maDichVu LIKE %:keyword% OR " +
            "CAST(c.soLuong AS string) LIKE %:keyword% OR " +
            "CAST(c.thanhTien AS string) LIKE %:keyword%)")
    List<ChiTietSuDungDichVu> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT c FROM ChiTietSuDungDichVu c " +
            "WHERE (:minSL IS NULL OR c.soLuong >= :minSL) " +
            "AND (:maxSL IS NULL OR c.soLuong <= :maxSL)")
    List<ChiTietSuDungDichVu> filterBySoLuong(@Param("minSL") Integer minSL, @Param("maxSL") Integer maxSL);

    @Query("SELECT d.giaDichVu " +
       "FROM ChiTietSuDungDichVu c " +
       "RIGHT JOIN c.dichVu d")
        List<BigDecimal> getgiaDichVu();

        @Query("SELECT COALESCE(SUM(ct.thanhTien), 0) " +
        "FROM ChiTietSuDungDichVu ct " +
        "WHERE ct.suDungDichVu.maSDDV = :maSDDV")
        BigDecimal sumThanhTienByMaSDDV(@Param("maSDDV") String maSDDV);
 
}