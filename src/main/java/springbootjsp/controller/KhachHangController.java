package springbootjsp.controller;

import springbootjsp.model.KhachHang;
import springbootjsp.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/khachhang")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    // Hiển thị danh sách khách hàng
    @GetMapping
    public String listKhachHang(Model model) {
        List<KhachHang> khachHangList = khachHangService.getAllKhachHang();
        model.addAttribute("khachHangList", khachHangList);
        // return "khachhang-list";
        return "khachhang/list-wrapper";
    }

    // Hiển thị form thêm khách hàng
    @GetMapping("/add")
    public String showAddForm(Model model) {
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKhachHang(khachHangService.generateNextMaKhachHang());
        model.addAttribute("khachHang", khachHang);
        model.addAttribute("gioiTinhList", KhachHang.GioiTinh.values());
        model.addAttribute("tinhTrangKhachList", KhachHang.TinhTrangKhach.values());
        model.addAttribute("isEdit", false);
        return "khachhang/form-wrapper";
    }


    // Hiển thị form sửa khách hàng
    @GetMapping("/edit/{maKhachHang}")
    public String showEditForm(@PathVariable String maKhachHang, Model model) {
        Optional<KhachHang> khachHang = khachHangService.getKhachHangById(maKhachHang);
        if (khachHang.isPresent()) {
            model.addAttribute("khachHang", khachHang.get());
            model.addAttribute("gioiTinhList", KhachHang.GioiTinh.values());
            model.addAttribute("tinhTrangKhachList", KhachHang.TinhTrangKhach.values());
            model.addAttribute("isEdit", true);
            return "khachhang/form-wrapper";
        }
        return "redirect:/khachhang";
    }

    // Xử lý thêm hoặc sửa khách hàng
    @PostMapping("/save")
    public String saveKhachHang(@ModelAttribute KhachHang khachHang) {
        khachHangService.saveKhachHang(khachHang);
        return "redirect:/khachhang";
    }

    // Xử lý xóa khách hàng
    @GetMapping("/delete/{maKhachHang}")
    public String deleteKhachHang(@PathVariable String maKhachHang) {
        khachHangService.deleteKhachHang(maKhachHang);
        return "redirect:/khachhang";
    }
}