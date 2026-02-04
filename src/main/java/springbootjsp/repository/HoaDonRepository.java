package springbootjsp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springbootjsp.model.HoaDon;
import springbootjsp.model.ThanhToan;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {

    @Query("""
        select h
        from HoaDon h
        left join h.maNhanVien nv
        left join h.khachHang kh
        where (:kw is null or :kw = '' 
               or lower(h.maHoaDon) like lower(concat('%', :kw, '%'))
               or (nv is not null and lower(nv.maNhanVien) like lower(concat('%', :kw, '%')))
               or (kh is not null and lower(kh.maKhachHang) like lower(concat('%', :kw, '%'))))
        order by h.maHoaDon
    """)
    List<HoaDon> searchByKeyword(@Param("kw") String kw);
    boolean existsByKhachHang_MaKhachHangAndThanhToan(String maKhachHang, ThanhToan thanhToan);

    boolean existsByKhachHang_MaKhachHangAndThanhToanAndMaHoaDonNot(
            String maKhachHang, ThanhToan thanhToan, String maHoaDon);
    boolean existsByKhachHang_MaKhachHang(String maKhachHang);


    
}
