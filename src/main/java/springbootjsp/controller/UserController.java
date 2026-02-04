package springbootjsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springbootjsp.model.User;
import springbootjsp.service.NhanVienService;
import springbootjsp.service.RoleService;
import springbootjsp.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private NhanVienService nhanVienService;

    // Show all users
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsersEntity());
        // return "user-list";
        return "users/list-wrapper";
    }

    // Show add form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());

        // Thêm danh sách roles và nhân viên vào model
        model.addAttribute("roles", roleService.getAllRoles());
//        List<NhanVien> nhanViens = nhanVienService.getNhanVienChuaCoUser();
//        model.addAttribute("nhanViens", nhanViens);
        return "users/form-wrapper";
        // return "user-form";
    }

    // Save user (add or update)
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);

        // Thêm danh sách roles và nhân viên vào model
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("nhanViens", nhanVienService.getAllNhanVien());

        return "users/form-wrapper";
        // return "user-form";
    }

    // Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
