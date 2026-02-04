package springbootjsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springbootjsp.model.KiemTraChiTiet;

import java.math.BigDecimal;
// import java.time.LocalDate;
import java.util.List;

@Repository
public interface KiemTraChiTietRepository extends JpaRepository<KiemTraChiTiet, KiemTraChiTiet.KiemTraChiTietId> {

    // ĐÃ SỬA: Chuyển đổi Enum sang String để sử dụng toán tử LIKE
    @Query("SELECT k FROM KiemTraChiTiet k " +
            "WHERE (:keyword IS NULL OR " +
            "k.id.maKiemTra LIKE %:keyword% OR " +
            "k.id.maThietBi LIKE %:keyword% OR " +
            "k.maPhong LIKE %:keyword% OR " +
            "CAST(k.tinhTrang AS string) LIKE %:keyword% OR " +
            "CAST(k.soLuongHong AS string) LIKE %:keyword% OR " +
            "k.ghiChu LIKE %:keyword%)")
    List<KiemTraChiTiet> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT k FROM KiemTraChiTiet k " +
            "WHERE :tinhTrang IS NULL OR k.tinhTrang = :tinhTrang")
    List<KiemTraChiTiet> filterByTinhTrang(@Param("tinhTrang") KiemTraChiTiet.TinhTrang tinhTrang);
    @Query("""
        select coalesce(sum(ct.denBu), 0)
        from KiemTraChiTiet ct
        where ct.kiemTraPhong.maKiemTra = :ma
        """)
 BigDecimal sumTienDenBuByMaKiemTra(@Param("ma") String maKiemTra);
 
}