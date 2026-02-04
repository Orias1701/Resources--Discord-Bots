package springbootjsp.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springbootjsp.model.Phong;

@Repository
public interface PhongRepository extends JpaRepository<Phong, String> {

    // Tim kiem theo keyword (ma phong, ten phong, mo ta)
    @Query("SELECT p FROM Phong p " +
            "WHERE (:keyword IS NULL OR p.maPhong LIKE %:keyword% " +
            "OR p.tenPhong LIKE %:keyword% " +
            "OR p.moTa LIKE %:keyword%)")
    List<Phong> searchByKeyword(String keyword);

    // Loc theo tinh trang phong
    @Query("SELECT p FROM Phong p WHERE (:tinhTrang IS NULL OR p.tinhTrangPhong = :tinhTrang)")
    List<Phong> filterByTinhTrang(Phong.TinhTrangPhong tinhTrang);

    // Lay max maPhong de sinh ma moi
    @Query("SELECT MAX(p.maPhong) FROM Phong p")
    String findMaxMaPhong();

    @Query(value = """
        SELECT lp.GiaLoai
        FROM a5_phong p
        JOIN a4_loaiphong lp ON p.MaLoai = lp.MaLoai
        WHERE p.MaPhong = :maPhong
        """, nativeQuery = true)
    BigDecimal findGiaLoaiByMaPhong(@Param("maPhong") String maPhong);

    List<Phong> findByTinhTrangPhong(Phong.TinhTrangPhong tinhTrangPhong);

}
