package springbootjsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springbootjsp.model.Role;
import springbootjsp.service.RoleService;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        // return "role-list";
        return "roles/list-wrapper";
    }

    // Form thêm role
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/form-wrapper";
    }

    // Lưu role
    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") Role role) {
        roleService.saveRole(role);
        return "redirect:/roles";
    }

    // Sửa role
    @GetMapping("/edit/{id}")
    public String editRole(@PathVariable("id") long id, Model model) {
        Role role = roleService.getRoleById(id);
        model.addAttribute("role", role);
        return "roles/form-wrapper";
    }

    // Xóa role
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") long id) {
        roleService.deleteRole(id);
        return "redirect:/roles";
    }
}
