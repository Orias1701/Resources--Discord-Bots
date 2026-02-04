package springbootjsp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springbootjsp.model.DatPhong;
import springbootjsp.model.ThanhToan;

@Repository
public interface DatPhongRepository extends JpaRepository<DatPhong, String> {

    /** Lấy mã lớn nhất để sinh mã mới (vd: DP001) */
    @Query("SELECT MAX(dp.maDatPhong) FROM DatPhong dp")
    String findMaxMaDatPhong();

    /**
     * Kiểm tra xung đột khoảng thời gian cho cùng một phòng.
     * Overlap: (start < existing_end OR existing_end IS NULL) AND (end > existing_start)
     */
    @Query("""
        SELECT dp
        FROM DatPhong dp
        JOIN dp.maPhong p
        WHERE p.maPhong = :maPhong
          AND (:excludeId IS NULL OR dp.maDatPhong <> :excludeId)
          AND (
                (:start < dp.ngayTraPhong OR dp.ngayTraPhong IS NULL)
            AND (:end   > dp.ngayNhanPhong)
          )
          AND dp.tinhTrang IN :activeStatuses
        """)
    List<DatPhong> findConflictsForRoom(
            @Param("maPhong") String maPhong,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("excludeId") String excludeId,
            @Param("activeStatuses") List<DatPhong.TinhTrang> activeStatuses
    );

    /** Lọc theo khoảng thời gian (đón/ trả) – tham số tùy chọn. */
    @Query("""
        SELECT dp
        FROM DatPhong dp
        WHERE (:from IS NULL OR dp.ngayNhanPhong >= :from)
          AND (:to   IS NULL OR (dp.ngayTraPhong IS NOT NULL AND dp.ngayTraPhong <= :to))
        ORDER BY dp.ngayNhanPhong DESC
        """)
    List<DatPhong> filterByDateRange(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    /** Search theo keyword + lọc trạng thái (tùy chọn) + chỉ lấy đơn quá hạn (tùy chọn) */
    @Query("""
        SELECT dp
        FROM DatPhong dp
        LEFT JOIN dp.maPhong p
        LEFT JOIN dp.maKhachHang kh
        WHERE
          (:keyword IS NULL OR
             LOWER(dp.maDatPhong)   LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(p.maPhong)       LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(kh.maKhachHang)  LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND ( :hasStatuses = false OR dp.tinhTrang IN :statuses )
        AND ( :onlyOverdue = false
              OR (dp.ngayTraPhong IS NOT NULL AND dp.ngayHenTra IS NOT NULL AND dp.ngayTraPhong > dp.ngayHenTra) )
        ORDER BY dp.ngayNhanPhong DESC
        """)
    List<DatPhong> search(
        @Param("keyword") String keyword,
        @Param("statuses") List<DatPhong.TinhTrang> statuses,
        @Param("hasStatuses") boolean hasStatuses,
        @Param("onlyOverdue") boolean onlyOverdue
    );

    /** Lọc theo tình trạng (tham số tùy chọn) */
    @Query("SELECT d FROM DatPhong d WHERE (:tt IS NULL OR d.tinhTrang = :tt)")
    List<DatPhong> filterByTinhTrang(@Param("tt") DatPhong.TinhTrang tinhTrang);

    /** Booking online đang 'ĐANG_ĐỢI' */
    @Query("SELECT d FROM DatPhong d WHERE d.cachDat = springbootjsp.model.DatPhong.CachDat.DAT_ONLINE AND d.tinhTrang = springbootjsp.model.DatPhong.TinhTrang.DANG_DOI")
    List<DatPhong> findOnlineDangDoi();

    /** Booking đang sử dụng */
    @Query("SELECT d FROM DatPhong d WHERE d.tinhTrang = springbootjsp.model.DatPhong.TinhTrang.DANG_SU_DUNG")
    List<DatPhong> findDangSuDung();

    /** Lọc theo khoảng thời gian nhận phòng */
    @Query("""
           SELECT d FROM DatPhong d
           WHERE (:from IS NULL OR d.ngayNhanPhong >= :from)
             AND (:to   IS NULL OR d.ngayNhanPhong <= :to)
           """)
    List<DatPhong> filterByNhanPhongRange(@Param("from") LocalDateTime from,
                                          @Param("to")   LocalDateTime to);

    /** Đếm số booking đang hoạt động theo phòng */
    @Query("""
            SELECT COUNT(d) FROM DatPhong d
            WHERE d.maPhong.maPhong = :maPhong
              AND d.tinhTrang IN (springbootjsp.model.DatPhong.TinhTrang.DANG_SU_DUNG,
                                  springbootjsp.model.DatPhong.TinhTrang.DANG_DOI)
        """)
    long countActiveByPhong(@Param("maPhong") String maPhong);

    /** Lấy danh sách theo nhiều trạng thái */
    List<DatPhong> findByTinhTrangIn(List<DatPhong.TinhTrang> statuses);
    @Query("""
        select count(dp) 
        from DatPhong dp 
        where dp.maKhachHang.maKhachHang = :maKH 
          and dp.thanhToan = springbootjsp.model.ThanhToan.CHUA_THANH_TOAN
          and (:excludeId is null or dp.maDatPhong <> :excludeId)
    """)
    long countUnpaidByCustomer(@Param("maKH") String maKH, @Param("excludeId") String excludeId);
    // DatPhongRepository
    List<DatPhong> findByMaPhong_MaPhongAndTinhTrangIn(
      String maPhong, List<DatPhong.TinhTrang> statuses);
     @Query("""
        select dp
        from DatPhong dp
        where dp.maKhachHang.maKhachHang = :maKh
          and (dp.thanhToan is null or dp.thanhToan <> springbootjsp.model.ThanhToan.DA_THANH_TOAN)
          and not exists (
              select 1 from HoaDonChiTiet ct
              where ct.datPhong = dp
                and (ct.thanhToan is null or ct.thanhToan <> springbootjsp.model.ThanhToan.DA_THANH_TOAN)
                and (:excludeHdctId is null or ct.id <> :excludeHdctId)
          )
        order by dp.ngayNhanPhong desc
        """)
    List<DatPhong> findEligibleForHdct(
        @Param("maKh") String maKh,
        @Param("excludeHdctId") Long excludeHdctId
    );
    List<DatPhong> findByThanhToan(ThanhToan thanhToan);
    @Query("""
      SELECT dp
      FROM DatPhong dp
      WHERE dp.maKhachHang.maKhachHang = :maKh
        AND dp.thanhToan = :tt
  """)
  List<DatPhong> findByKhachAndThanhToan(@Param("maKh") String maKhachHang,
                                         @Param("tt") ThanhToan thanhToan);
  List<DatPhong> findByMaKhachHang_MaKhachHangAndThanhToan(String maKhachHang, ThanhToan thanhToan);
}
