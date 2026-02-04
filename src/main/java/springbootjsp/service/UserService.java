package springbootjsp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import springbootjsp.model.NhanVien;
import springbootjsp.model.User;
import springbootjsp.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Thêm PasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Lấy danh sách UserDTO
    public List<User> getAllUsersEntity() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    // ✅ Cập nhật logic lưu user — có mã hóa
    public User saveUser(User user) {
        // Nếu đang UPDATE
        if (user.getId() != null) {
            User existing = userRepository.findById(user.getId()).orElse(null);
            if (existing != null) {
                // Nếu mật khẩu mới trống => giữ nguyên mật khẩu cũ
                if (user.getPassword() == null || user.getPassword().isBlank()) {
                    user.setPassword(existing.getPassword());
                }
                // Nếu có mật khẩu mới => mã hóa lại
                else {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            } else {
                // Nếu ID không tồn tại trong DB (hiếm khi), coi như tạo mới
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        // Nếu đang tạo mới user
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findUserByNhanVien(NhanVien nv) {
        return userRepository.findByNhanVien(nv);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
