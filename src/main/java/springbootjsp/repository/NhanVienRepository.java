package springbootjsp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import springbootjsp.model.NhanVien;

public interface NhanVienRepository extends JpaRepository<NhanVien, String> {

    @Query("SELECT MAX(n.maNhanVien) FROM NhanVien n")
    String findMaxMaNhanVien();

    List<NhanVien>findByChucVu(String chucVu);

    @Query("SELECT n FROM NhanVien n WHERE n.maNhanVien = ?1")
    NhanVien findByMaNhanVien(String maNhanVien);

    NhanVien findByTenNhanVien(String tenNhanVien);
}
