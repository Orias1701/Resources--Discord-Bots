package springbootjsp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import springbootjsp.model.ThietBiPhong;

public interface ThietBiPhongRepository extends JpaRepository<ThietBiPhong, Long> {

    // Tìm kiếm theo MaPhong hoặc MaThietBi + lọc trạng thái (tham số optional)
    @Query("""
           SELECT t FROM ThietBiPhong t
           WHERE (:keyword IS NULL OR :keyword = '' 
                  OR t.phong.maPhong    LIKE CONCAT('%', :keyword, '%')
                  OR t.thietBi.maThietBi LIKE CONCAT('%', :keyword, '%'))
             AND (:trangThai IS NULL OR t.trangThai = :trangThai)
           """)
    List<ThietBiPhong> search(
            @Param("keyword") String keyword,
            @Param("trangThai") ThietBiPhong.TrangThai trangThai
    );

    // Hỗ trợ kiểm tra/gộp dữ liệu theo UNIQUE(MaPhong, MaThietBi)
    boolean existsByPhong_MaPhongAndThietBi_MaThietBi(String maPhong, String maThietBi);

    Optional<ThietBiPhong> findByPhong_MaPhongAndThietBi_MaThietBi(String maPhong, String maThietBi);

    // Tiện ích lọc nhanh
    List<ThietBiPhong> findByPhong_MaPhong(String maPhong);
    @Query("SELECT t.soLuong FROM ThietBiPhong t WHERE t.thietBi.maThietBi = :maThietBi")
    Integer findSoLuongByMaThietBi(@Param("maThietBi") String maThietBi);
    List<ThietBiPhong> findByThietBi_MaThietBi(String maThietBi);
    

}
