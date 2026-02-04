package springbootjsp.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springbootjsp.model.HoaDon;
import springbootjsp.model.User;
import springbootjsp.repository.HoaDonRepository;
import springbootjsp.service.HoaDonService;
import springbootjsp.service.KhachHangService;
import springbootjsp.service.NhanVienService;

@Controller
@RequestMapping("/hoadon")
public class HoaDonController {

    private final HoaDonService service;
    private final NhanVienService nhanVienService;
    private final KhachHangService khachHangService;
    // private final HoaDonRepository hoaDonRepository;

    public HoaDonController(HoaDonService service,
                            NhanVienService nhanVienService,
                            KhachHangService khachHangService,HoaDonRepository hoaDonRepository) {
        this.service = service;
        this.nhanVienService = nhanVienService;
        this.khachHangService = khachHangService;
        // this.hoaDonRepository = hoaDonRepository;
    }

    // DANH SÁCH + TÌM KIẾM
    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        List<HoaDon> list = (keyword != null && !keyword.isBlank())
                ? service.search(keyword)
                : service.getAll();

        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);
//        return "hoadon-list";
         return "hoadon/list-wrapper";
    }

    // FORM THÊM / SỬA
    @GetMapping("/form")
    public String form(@RequestParam(required = false) String maHoaDon,
                       Model model,
                       HttpSession session) {

        HoaDon hd;
        if (maHoaDon != null && !maHoaDon.isBlank()) {
            hd = service.getById(maHoaDon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn: " + maHoaDon));
        } else {
            hd = new HoaDon();
            hd.setMaHoaDon(service.generateNextMaHoaDon());
        }

        // ✅ Lấy thông tin nhân viên từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null && loggedInUser.getNhanVien() != null) {
            hd.setMaNhanVien(loggedInUser.getNhanVien());
            model.addAttribute("nhanVienDangNhap", loggedInUser.getNhanVien());
        }

        model.addAttribute("hd", hd);
        model.addAttribute("khachHangList", khachHangService.getAllKhachHang());
        return "hoadon/form-wrapper";
    }

    // LƯU
    @PostMapping("/save")
    public String save(@ModelAttribute HoaDon hd,
                       HttpSession session,
                       RedirectAttributes ra) {
        try {
            // ✅ Gán nhân viên đang đăng nhập vào hóa đơn
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser != null && loggedInUser.getNhanVien() != null) {
                hd.setMaNhanVien(loggedInUser.getNhanVien());
            }

            // ✅ Lưu hóa đơn
            service.save(hd);
            ra.addFlashAttribute("message", "Lưu hóa đơn thành công");
            return "redirect:/hoadon";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi lưu hóa đơn: " + e.getMessage());
            return "redirect:/hoadon/form" + (hd.getMaHoaDon() != null ? "?maHoaDon=" + hd.getMaHoaDon() : "");
        }
    }


    // XÓA
    @GetMapping("/delete")
    public String delete(@RequestParam String maHoaDon, RedirectAttributes ra) {
        try {
            service.deleteById(maHoaDon);
            ra.addFlashAttribute("message", "Xóa hóa đơn thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/hoadon";
    }

    // NÚT: chuyển hóa đơn sang ĐÃ_THANH_TOÁN
    @PostMapping("/mark-paid")
    public String markPaid(@RequestParam String maHoaDon, RedirectAttributes ra) {
        try {
            service.markHoaDonPaid(maHoaDon);
            ra.addFlashAttribute("message", "Đã chuyển hóa đơn sang ĐÃ_THANH_TOÁN");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/hoadon";
    }

    @GetMapping("/in/{maHoaDon}")
    public String inHoaDon(@PathVariable String maHoaDon, Model model) {
        Map<String, Object> data = service.getHoaDonVaChiTiet(maHoaDon);
        model.addAttribute("hoaDon", data.get("hoaDon"));
        model.addAttribute("chiTiet", data.get("chiTiet"));
        return "hoadon/in"; // file JSP in mẫu
    }

}
