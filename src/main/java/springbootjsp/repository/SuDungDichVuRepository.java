package springbootjsp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springbootjsp.model.SuDungDichVu;
import springbootjsp.model.ThanhToan;

@Repository
public interface SuDungDichVuRepository extends JpaRepository<SuDungDichVu, String> {

    // Tim kiem theo keyword (MaSDDV hoac ten khach hang)
    @Query("SELECT s FROM SuDungDichVu s " +
            "WHERE (:keyword IS NULL OR s.maSDDV LIKE %:keyword% " +
            "OR s.khachHang.tenKhachHang LIKE %:keyword%)")
    List<SuDungDichVu> searchByKeyword(String keyword);

    // Loc theo ngay su dung
    @Query("SELECT s FROM SuDungDichVu s " +
            "WHERE (:startDate IS NULL OR s.ngaySDDV >= :startDate) " +
            "AND (:endDate IS NULL OR s.ngaySDDV <= :endDate)")
    List<SuDungDichVu> filterByDate(LocalDate startDate, LocalDate endDate);

    @Query("SELECT s.maSDDV, COALESCE(SUM(ct.thanhTien), 0) " +
           "FROM SuDungDichVu s " +
           "LEFT JOIN ChiTietSuDungDichVu ct ON ct.suDungDichVu = s " +
           "GROUP BY s.maSDDV")
    List<Object[]> getTongTienTheoSDDV();

        @Query("SELECT MAX(s.maSDDV) FROM SuDungDichVu s")
        String findMaxMaSDDV();

    List<SuDungDichVu> findByThanhToan(ThanhToan thanhToan);

    SuDungDichVu findByMaSDDV(String maSDDV);
    @Query("""
        SELECT s
        FROM SuDungDichVu s
        WHERE s.khachHang.maKhachHang = :maKh
          AND s.thanhToan = :tt
    """)
    List<SuDungDichVu> findByKhachAndThanhToan(@Param("maKh") String maKhachHang,
                                               @Param("tt") ThanhToan thanhToan);
    List<SuDungDichVu> findByKhachHang_MaKhachHangAndThanhToan(String khachHang, ThanhToan thanhToan);
}
