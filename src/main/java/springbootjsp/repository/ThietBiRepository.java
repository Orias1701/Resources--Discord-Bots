package springbootjsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springbootjsp.model.ThietBi;

import java.util.List;

@Repository
public interface ThietBiRepository extends JpaRepository<ThietBi, String> {

    // Tim kiem gop chung theo ma, ten, don gia, den bu
    @Query("SELECT t FROM ThietBi t " +
            "WHERE (:keyword IS NULL OR " +
            "t.maThietBi LIKE %:keyword% OR " +
            "t.tenThietBi LIKE %:keyword% OR " +
            "CAST(t.donGia AS string) LIKE %:keyword% OR " +
            "CAST(t.denBu AS string) LIKE %:keyword%)")
    List<ThietBi> searchByKeyword(String keyword);

    @Query("SELECT MAX(n.maThietBi) FROM ThietBi n")
    String findMaxMaThietBi();
}
