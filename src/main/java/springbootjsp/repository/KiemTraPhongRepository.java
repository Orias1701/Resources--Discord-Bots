package springbootjsp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springbootjsp.model.KiemTraPhong;
import springbootjsp.model.ThanhToan;

@Repository
public interface KiemTraPhongRepository extends JpaRepository<KiemTraPhong, String> {

    // Tìm kiếm theo mã kiểm tra, mã phòng, mã nhân viên hoặc ghi chú
    @Query("""
    select distinct k
    from KiemTraPhong k
    left join k.phong p
    left join DatPhong d on d.maPhong = p   
    where (:kw is null or :kw = ''
        or k.maKiemTra like :kw
        or p.maPhong like :kw
        or k.maNhanVien.maNhanVien like :kw
        or k.ghiChu like :kw
        or d.maDatPhong like :kw
    )
    """)
    List<KiemTraPhong> searchByKeyword(@Param("kw") String kw);


    // Lọc theo trạng thái
    @Query("SELECT k FROM KiemTraPhong k WHERE (:trangThai IS NULL OR k.trangThai = :trangThai)")
    List<KiemTraPhong> filterByTrangThai(KiemTraPhong.TrangThai trangThai);

    // Lấy max MaKiemTra để sinh mã mới
    @Query("SELECT MAX(k.maKiemTra) FROM KiemTraPhong k")
    String findMaxMaKiemTra();

    // Tìm tất cả các KiemTraPhong dựa trên trạng thái thanh toán.
    List<KiemTraPhong> findByThanhToan(ThanhToan thanhToan);

    // Tìm một KiemTraPhong cụ thể dựa trên maKiemTra.
    KiemTraPhong findByMaKiemTra(String maKiemTra);
    List<KiemTraPhong> findByMaKiemTraOrderByNgayKiemTraDesc(String maKiemTra);
    
    Optional<KiemTraPhong> findFirstByDatPhong_MaDatPhongAndThanhToanOrderByNgayKiemTraDesc(
        String maDatPhong, ThanhToan thanhToan);
}
