package springbootjsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springbootjsp.model.NhanVien;
import springbootjsp.model.User;
import springbootjsp.service.NhanVienService;
import springbootjsp.service.RoleService;
import springbootjsp.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/nhanvien")
public class NhanVienController {

    @Autowired
    private NhanVienService service;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping
    public String listNhanVien(Model model) {
        List<NhanVien> list = service.getAllNhanVien();
        model.addAttribute("listNhanVien", list);
        // return "nhanvien-list";
        return "nhanvien/list-wrapper";
    }

    @GetMapping("/add")
    public String addNhanVienForm(Model model) {
        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(service.generateNextMaNhanVien());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("nhanVien", nv);
        // return "nhanvien-form";
        return "nhanvien/form-wrapper";
    }


    // Trong NhanVienController
    @PostMapping("/save")
    public String saveNhanVien(@ModelAttribute("nhanVien") NhanVien nv,
                               @RequestParam("roleId") Long roleId) {

        // 1️⃣ Lưu nhân viên vào DB
        service.saveNhanVien(nv);

        // 2️⃣ Kiểm tra user tương ứng
        Optional<User> existingUser = userService.findUserByNhanVien(nv);
        User user;

        if (existingUser.isPresent()) {
            // --- CẬP NHẬT USER CŨ ---
            user = existingUser.get();

            // Giữ nguyên mật khẩu cũ để tránh bị encode lại lần 2
            String oldPassword = user.getPassword();
            user.setPassword(oldPassword);

            System.out.println(">>> [DEBUG] Nhân viên " + nv.getMaNhanVien() + " đã có user, giữ nguyên mật khẩu cũ.");

        } else {
            // --- TẠO USER MỚI ---
            user = new User();
            user.setUsername(nv.getMaNhanVien()); // Username = mã NV

            // ✅ Mật khẩu mặc định = SĐT (fallback nếu SDT null)
            String rawPassword = nv.getSdt();
            if (rawPassword == null || rawPassword.isBlank()) rawPassword = "123";
            System.out.println(">>> [DEBUG] Raw password cho " + nv.getMaNhanVien() + " = " + rawPassword);
            user.setPassword(rawPassword); // ❌ không encode ở đây

        }

        // 3️⃣ Gán role và nhân viên
        user.setNhanVien(nv);
        user.setRole(roleService.getRoleById(roleId));

        // 4️⃣ Lưu user
        userService.saveUser(user);

        System.out.println(">>> [SUCCESS] Đã lưu user " + user.getUsername() + " với roleId=" + roleId);
        return "redirect:/nhanvien";
    }


    @GetMapping("/edit/{id}")
    public String editNhanVien(@PathVariable("id") String maNV, Model model) {
        NhanVien nv = service.getNhanVienById(maNV);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("nhanVien", nv);
        // return "nhanvien-form";
        return "nhanvien/form-wrapper";
    }

    @GetMapping("/delete/{id}")
    public String deleteNhanVien(@PathVariable("id") String maNV) {
        service.deleteNhanVien(maNV);
        return "redirect:/nhanvien";
    }
}
