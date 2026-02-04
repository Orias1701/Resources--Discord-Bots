package springbootjsp.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springbootjsp.model.HoaDonChiTiet;
import springbootjsp.model.ThanhToan;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Long> {

    // TIỀN KIỂM TRA cho 1 dòng HDCT (nếu là dòng KIỂM TRA; ngược lại trả về Optional.empty()/null)
    @Query(value = """
        SELECT k.TienKiemTra
        FROM a3_hoadonchitiet c
        LEFT JOIN a9_kiemtraphong k ON c.MaKiemTra = k.MaKiemTra
        WHERE c.Id = :hdctId
        """, nativeQuery = true)
    Optional<BigDecimal> findTienKiemTraByHdctId(@Param("hdctId") Long hdctId);

    // TỔNG TIỀN SDDV cho 1 dòng HDCT (nếu là dòng DỊCH VỤ; ngược lại null)
    @Query(value = """
        SELECT s.TongTien
        FROM a3_hoadonchitiet c
        LEFT JOIN b2_sddv s ON c.MaSDDV = s.MaSDDV
        WHERE c.Id = :hdctId
        """, nativeQuery = true)
    Optional<BigDecimal> findTongTienSddvByHdctId(@Param("hdctId") Long hdctId);

    // (Tiện) SỐ TIỀN của 1 dòng (cộng cả kiểm tra + sddv; nếu cả 2 đều null thì 0)
    @Query(value = """
        SELECT COALESCE(k.TienKiemTra,0) + COALESCE(s.TongTien,0)
        FROM a3_hoadonchitiet c
        LEFT JOIN a9_kiemtraphong k ON c.MaKiemTra = k.MaKiemTra
        LEFT JOIN b2_sddv         s ON c.MaSDDV     = s.MaSDDV
        WHERE c.Id = :hdctId
        """, nativeQuery = true)
    BigDecimal amountOfRow(@Param("hdctId") Long hdctId);

    // TỔNG tiền kiểm tra của 1 hóa đơn
    @Query(value = """
        SELECT COALESCE(SUM(k.TienKiemTra), 0)
        FROM a3_hoadonchitiet c
        LEFT JOIN a9_kiemtraphong k ON c.MaKiemTra = k.MaKiemTra
        WHERE c.MaHoaDon = :maHoaDon
        """, nativeQuery = true)
    BigDecimal sumTienKiemTraByHoaDon(@Param("maHoaDon") String maHoaDon);

    // TỔNG tiền dịch vụ của 1 hóa đơn
    @Query(value = """
        SELECT COALESCE(SUM(s.TongTien), 0)
        FROM a3_hoadonchitiet c
        LEFT JOIN b2_sddv s ON c.MaSDDV = s.MaSDDV
        WHERE c.MaHoaDon = :maHoaDon
        """, nativeQuery = true)
    BigDecimal sumTongTienSddvByHoaDon(@Param("maHoaDon") String maHoaDon);

    // TỔNG tất cả dòng của 1 hóa đơn (kiểm tra + dịch vụ; phòng nếu bạn thêm sau thì cộng thêm)
    @Query(value = """
        SELECT COALESCE(SUM(COALESCE(k.TienKiemTra,0) + COALESCE(s.TongTien,0)), 0)
        FROM a3_hoadonchitiet c
        LEFT JOIN a9_kiemtraphong k ON c.MaKiemTra = k.MaKiemTra
        LEFT JOIN b2_sddv         s ON c.MaSDDV     = s.MaSDDV
        WHERE c.MaHoaDon = :maHoaDon
        """, nativeQuery = true)
    BigDecimal sumTatCaDongByHoaDon(@Param("maHoaDon") String maHoaDon);
    @Query("SELECT COALESCE(SUM(h.thanhTien), 0) FROM HoaDonChiTiet h WHERE h.hoaDon.maHoaDon = :maHoaDon")
    BigDecimal sumThanhTienByHoaDon(@Param("maHoaDon") String maHoaDon);
    
    List<HoaDonChiTiet> findByHoaDon_MaHoaDon(String maHoaDon);

    // (Tuỳ chọn) nếu bạn muốn JPQL rõ ràng:
    @Query("SELECT c FROM HoaDonChiTiet c WHERE c.hoaDon.maHoaDon = :maHoaDon")
    List<HoaDonChiTiet> findAllByHoaDonMa(@Param("maHoaDon") String maHoaDon);


    Optional<HoaDonChiTiet> findByKiemTraPhong_MaKiemTra(String maKiemTra);
    Optional<HoaDonChiTiet> findBySuDungDichVu_MaSDDV(String maSDDV);

    @Query("SELECT SUM(hdct.tienDatPhong + hdct.tienKiemTra + hdct.tienSddv) " +
            "FROM HoaDonChiTiet hdct WHERE hdct.hoaDon.maHoaDon = :maHoaDon")
    BigDecimal sumTienByHoaDon(@Param("maHoaDon") String maHoaDon);

    // Trong repository
    @Query("select h from HoaDonChiTiet h " +
            "left join fetch h.kiemTraPhong kt " +
            "left join fetch h.suDungDichVu dv " +
            "where h.hoaDon.maHoaDon = :maHoaDon")
    List<HoaDonChiTiet> findHoaDonChiTietByMaHoaDonWithDetails(@Param("maHoaDon") String maHoaDon);

    // ===== THÊM PHƯƠNG THỨC MỚI NÀY =====
    // SỬA LẠI PHƯƠNG THỨC NÀY
    @Query("SELECT DISTINCT h FROM HoaDonChiTiet h " +
            "LEFT JOIN FETCH h.hoaDon " +
            "LEFT JOIN FETCH h.datPhong " +
            "LEFT JOIN FETCH h.suDungDichVu " +
            "LEFT JOIN FETCH h.kiemTraPhong")
    List<HoaDonChiTiet> findAllWithDetails();

    @Query("SELECT DISTINCT h FROM HoaDonChiTiet h " +
            "LEFT JOIN FETCH h.hoaDon " +
            "LEFT JOIN FETCH h.datPhong " +
            "LEFT JOIN FETCH h.suDungDichVu " +
            "LEFT JOIN FETCH h.kiemTraPhong " +
            "WHERE h.hoaDon.maHoaDon = :maHoaDon")
    List<HoaDonChiTiet> findByHoaDon_MaHoaDonWithDetails(@Param("maHoaDon") String maHoaDon);
    Optional<HoaDonChiTiet> findByHoaDon_MaHoaDonAndDatPhong_MaDatPhong(String maHoaDon, String maDatPhong);
    Optional<HoaDonChiTiet> findByHoaDon_MaHoaDonAndSuDungDichVu_MaSDDV(String maHoaDon, String maSDDV);
    Optional<HoaDonChiTiet> findByHoaDon_MaHoaDonAndKiemTraPhong_MaKiemTra(String maHoaDon, String maKiemTra);
    List<HoaDonChiTiet> findByDatPhong_MaDatPhong(String maDatPhong);
    @Query("""
        SELECT h FROM HoaDonChiTiet h
        WHERE h.hoaDon.maHoaDon = :maHoaDon
          AND ( (:maDatPhong IS NULL AND h.datPhong IS NULL) OR (h.datPhong.maDatPhong = :maDatPhong) )
          AND ( (:maSDDV IS NULL AND h.suDungDichVu IS NULL) OR (h.suDungDichVu.maSDDV = :maSDDV) )
          AND ( (:maKiemTra IS NULL AND h.kiemTraPhong IS NULL) OR (h.kiemTraPhong.maKiemTra = :maKiemTra) )
    """)
    List<HoaDonChiTiet> findSameCombo(
            @Param("maHoaDon") String maHoaDon,
            @Param("maDatPhong") String maDatPhong,
            @Param("maSDDV") String maSDDV,
            @Param("maKiemTra") String maKiemTra
    );
    @Query("select coalesce(sum(h.thanhTien), 0) " +
           "from HoaDonChiTiet h " +
           "where h.hoaDon.maHoaDon = :maHoaDon")
    BigDecimal sumThanhTienByMaHoaDon(@Param("maHoaDon") String maHoaDon);

     @Query("""
        select (count(h) > 0)
        from HoaDonChiTiet h
        where h.hoaDon.maHoaDon = :maHoaDon
          and h.thanhToan = :paid
          and h.datPhong.maDatPhong = :maDp
          and (:excludeId is null or h.id <> :excludeId)
    """)
    boolean existsPaidSiblingByMaDp(@Param("maHoaDon") String maHoaDon,
                                    @Param("maDp") String maDp,
                                    @Param("paid") ThanhToan paid,
                                    @Param("excludeId") Long excludeId);

    @Query("""
        select (count(h) > 0)
        from HoaDonChiTiet h
        where h.hoaDon.maHoaDon = :maHoaDon
          and h.thanhToan = :paid
          and h.suDungDichVu.maSDDV = :maSddv
          and (:excludeId is null or h.id <> :excludeId)
    """)
    boolean existsPaidSiblingByMaSddv(@Param("maHoaDon") String maHoaDon,
                                      @Param("maSddv") String maSddv,
                                      @Param("paid") ThanhToan paid,
                                      @Param("excludeId") Long excludeId);

    @Query("""
        select (count(h) > 0)
        from HoaDonChiTiet h
        where h.hoaDon.maHoaDon = :maHoaDon
          and h.thanhToan = :paid
          and h.kiemTraPhong.maKiemTra = :maKt
          and (:excludeId is null or h.id <> :excludeId)
    """)
    boolean existsPaidSiblingByMaKt(@Param("maHoaDon") String maHoaDon,
                                    @Param("maKt") String maKt,
                                    @Param("paid") ThanhToan paid,
                                    @Param("excludeId") Long excludeId);
                                    @Query("""
           select count(h) > 0
           from HoaDonChiTiet h
           where h.hoaDon.maHoaDon = :maHoaDon
             and h.datPhong.maDatPhong = :maDatPhong
             and (:excludeId is null or h.id <> :excludeId)
           """)
    boolean existsDuplicateDp(@Param("maHoaDon") String maHoaDon,
                              @Param("maDatPhong") String maDatPhong,
                              @Param("excludeId") Long excludeId);

    @Query("""
           select count(h) > 0
           from HoaDonChiTiet h
           where h.hoaDon.maHoaDon = :maHoaDon
             and h.suDungDichVu.maSDDV = :maSddv
             and (:excludeId is null or h.id <> :excludeId)
           """)
    boolean existsDuplicateSddv(@Param("maHoaDon") String maHoaDon,
                                @Param("maSddv") String maSddv,
                                @Param("excludeId") Long excludeId);

    @Query("""
           select count(h) > 0
           from HoaDonChiTiet h
           where h.hoaDon.maHoaDon = :maHoaDon
             and h.kiemTraPhong.maKiemTra = :maKt
             and (:excludeId is null or h.id <> :excludeId)
           """)
    boolean existsDuplicateKt(@Param("maHoaDon") String maHoaDon,
                              @Param("maKt") String maKt,
                              @Param("excludeId") Long excludeId);

    boolean existsByDatPhong_MaDatPhong(String maDatPhong);
    boolean existsBySuDungDichVu_MaSDDV(String maSDDV);
    boolean existsByKiemTraPhong_MaKiemTra(String maKiemTra);
                              
}
