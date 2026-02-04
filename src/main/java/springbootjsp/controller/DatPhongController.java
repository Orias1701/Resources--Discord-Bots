package springbootjsp.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springbootjsp.model.DatPhong;
import springbootjsp.model.User;
import springbootjsp.service.DatPhongService;
import springbootjsp.service.KhachHangService;
import springbootjsp.service.NhanVienService;
import springbootjsp.service.PhongService;

@Controller
@RequestMapping("/datphong")
public class DatPhongController {
    private final DatPhongService datPhongService;
    private final NhanVienService nhanVienService;
    private final KhachHangService khachHangService;
    private final PhongService phongService;
    public DatPhongController(DatPhongService datPhongService,
                             NhanVienService nhanVienService,
                             KhachHangService khachHangService,
                             PhongService phongService) {
        this.datPhongService = datPhongService;
        this.nhanVienService = nhanVienService;
        this.khachHangService = khachHangService;
        this.phongService = phongService;
    }
    @ModelAttribute("tinhTrangEnums")
    public springbootjsp.model.DatPhong.TinhTrang[] tinhTrangEnums() {
        return springbootjsp.model.DatPhong.TinhTrang.values();
    }
    @ModelAttribute("cachDatEnums")
    public springbootjsp.model.DatPhong.CachDat[] cachDatEnums() {
        return springbootjsp.model.DatPhong.CachDat.values();
    }
    @InitBinder("datPhong") // tên attribute mà @ModelAttribute hoặc form submit về
    public void disallowThanhToan(WebDataBinder binder) {
        binder.setDisallowedFields("thanhToan");
    }
    @GetMapping
    public String listDatPhong(
            Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String maNhanVien,
            @RequestParam(required = false) String maKhachHang,
            @RequestParam(required = false) String maPhong,
            @RequestParam(required = false) String tinhTrang,
            @RequestParam(required = false) BigDecimal minTienPhat,
            @RequestParam(required = false) BigDecimal maxTienPhat,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate
    ) {
        // Normalize chuỗi rỗng -> null để service lọc gọn hơn
        String kw          = (keyword     != null && !keyword.isBlank())     ? keyword.trim()     : null;
        String nv          = (maNhanVien  != null && !maNhanVien.isBlank())  ? maNhanVien.trim()  : null;
        String kh          = (maKhachHang != null && !maKhachHang.isBlank()) ? maKhachHang.trim() : null;
        String mp          = (maPhong     != null && !maPhong.isBlank())     ? maPhong.trim()     : null;
        String tt          = (tinhTrang   != null && !tinhTrang.isBlank())   ? tinhTrang.trim()   : null;

        // Bao trọn khoảng tới phút cuối cùng (tạo cảm giác inclusive cho UI)
        LocalDateTime toInclusive = (toDate != null) ? toDate.plusSeconds(59) : null;

        try {
            List<DatPhong> list = datPhongService.search(
                    kw, nv, kh, mp, tt, minTienPhat, maxTienPhat, fromDate, toInclusive
            );
            model.addAttribute("list", list);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Không thể tải danh sách: " + ex.getMessage());
            model.addAttribute("list", java.util.Collections.emptyList());
        }

        // Giữ lại filter để hiển thị trên form
        model.addAttribute("keyword", keyword);
        model.addAttribute("maNhanVien", maNhanVien);
        model.addAttribute("maKhachHang", maKhachHang);
        model.addAttribute("maPhong", maPhong);
        model.addAttribute("tinhTrang", tinhTrang);
        model.addAttribute("minTienPhat", minTienPhat);
        model.addAttribute("maxTienPhat", maxTienPhat);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        // Dữ liệu dropdown
        model.addAttribute("nhanVienList", nhanVienService.getAll());
        model.addAttribute("khachHangList", khachHangService.getAll());
        model.addAttribute("phongList", phongService.getAll());
//        return "datphong-list";
         return "datphong/list-wrapper";
    }


    @GetMapping("/form")
    public String formDatPhong(
            @RequestParam(required = false) String maDatPhong,
            Model model,
            HttpSession session) {

        DatPhong datPhong;
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (maDatPhong != null) {
            // 🔹 Nếu là chỉnh sửa
            datPhong = datPhongService.findById(maDatPhong);
            if (datPhong == null) {
                throw new RuntimeException("Đặt phòng không tồn tại");
            }
        } else {
            // 🔹 Nếu là thêm mới
            datPhong = new DatPhong();
            datPhong.setMaDatPhong(datPhongService.generateNextMaDatPhong());
            datPhong.setNgayNhanPhong(LocalDateTime.now());
            datPhong.setThanhToan(springbootjsp.model.ThanhToan.CHUA_THANH_TOAN);
            datPhong.setTienPhat(java.math.BigDecimal.ZERO);
            datPhong.setTongTien(java.math.BigDecimal.ZERO);

            // ✅ Gán nhân viên đang đăng nhập (nếu có)
            if (loggedInUser != null && loggedInUser.getNhanVien() != null) {
                datPhong.setMaNhanVien(loggedInUser.getNhanVien());
            }
        }

        // ✅ Dữ liệu cho các dropdown còn lại
        model.addAttribute("datPhong", datPhong);
        model.addAttribute("khachHangList", khachHangService.getAll());
        model.addAttribute("phongList", phongService.getAll());

        // ✅ Truyền thông tin nhân viên đăng nhập để hiển thị trong form
        if (loggedInUser != null && loggedInUser.getNhanVien() != null) {
            model.addAttribute("nhanVienDangNhap", loggedInUser.getNhanVien());
        }

        return "datphong/form-wrapper";
    }


    @PostMapping("/save")
    public String saveDatPhong(@ModelAttribute DatPhong datPhong,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            // ✅ Lấy nhân viên đang đăng nhập
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser != null && loggedInUser.getNhanVien() != null) {
                datPhong.setMaNhanVien(loggedInUser.getNhanVien());
            }

            datPhongService.save(datPhong);
            redirectAttributes.addFlashAttribute("successMessage", "Đặt phòng đã được lưu thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi lưu đặt phòng: " + e.getMessage());
        }
        return "redirect:/datphong";
    }

    @GetMapping("/delete")
    public String deleteDatPhong(@RequestParam String maDatPhong, RedirectAttributes redirectAttributes) {
        try {
            datPhongService.deleteById(maDatPhong);
            redirectAttributes.addFlashAttribute("successMessage", "Đặt phòng đã được xóa thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa đặt phòng: " + e.getMessage());
        }
        return "redirect:/datphong";
    }
}
