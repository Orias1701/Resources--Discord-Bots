package springbootjsp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import springbootjsp.model.KhachHang;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, String> {

    @Query("SELECT MAX(n.maKhachHang) FROM KhachHang n")
    String findMaxMaKhachHang();

    //Tìm khách hàng từ mã khách hàng
    @Query("SELECT n FROM KhachHang n WHERE n.maKhachHang = ?1")
    KhachHang findByMaKhachHang(KhachHang maKhachHang);
    
    
}
