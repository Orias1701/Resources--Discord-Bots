package springbootjsp.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import springbootjsp.model.NhanVien;
import springbootjsp.model.Role;
import springbootjsp.model.User;
import springbootjsp.repository.NhanVienRepository;
import springbootjsp.repository.RoleRepository;
import springbootjsp.repository.UserRepository;
import springbootjsp.service.NhanVienService; // 👈 thêm import

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NhanVienRepository nhanVienRepository;
    private final NhanVienService nhanVienService; // 👈 thêm

    public DataLoader(RoleRepository roleRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      NhanVienRepository nhanVienRepository,
                      NhanVienService nhanVienService) { // 👈 thêm
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.nhanVienRepository = nhanVienRepository;
        this.nhanVienService = nhanVienService; // 👈 thêm
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔹 DataLoader đang khởi tạo dữ liệu mặc định...");

        // 1️⃣ Tạo role ADMIN nếu chưa có
        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Quyền quản trị viên");
            roleRepository.save(adminRole);
            System.out.println("✅ Đã tạo role ADMIN");
        }

        // 2️⃣ Tạo user Admin nếu chưa có
        Optional<User> adminUser = userRepository.findByUsername("Admin");
        if (adminUser.isEmpty()) {

            // 🧩 Kiểm tra hoặc tạo nhân viên admin
            NhanVien adminNV = nhanVienRepository.findByTenNhanVien("Quản trị hệ thống");
            if (adminNV == null) {
                adminNV = new NhanVien();
                // 💡 Gán mã thủ công bằng hàm generateNextMaNhanVien()
                adminNV.setMaNhanVien(nhanVienService.generateNextMaNhanVien());
                adminNV.setTenNhanVien("Quản trị hệ thống");
                adminNV.setChucVu("Admin");
                adminNV.setGioiTinh(NhanVien.GioiTinh.NAM);
                adminNV.setSdt("0123456789");
                nhanVienRepository.save(adminNV);
                System.out.println("✅ Đã tạo nhân viên mặc định cho Admin: " + adminNV.getMaNhanVien());
            }

            // 🧩 Tạo tài khoản admin
            User user = new User();
            user.setUsername("Admin");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(adminRole);
            user.setNhanVien(adminNV); // ✅ gắn nhân viên này vào user
            userRepository.save(user);

            System.out.println("✅ Đã tạo user Admin (username=Admin, password=123456)");
        }

        System.out.println("🎯 DataLoader hoàn tất: đã tạo mặc định Role & User Admin nếu chưa có.");
    }
}
