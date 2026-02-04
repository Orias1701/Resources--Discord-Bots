package springbootjsp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import springbootjsp.model.NhanVien;
import springbootjsp.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByNhanVien(NhanVien nhanVien);

}
