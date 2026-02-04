package springbootjsp.controller;

import java.time.LocalDate;

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

import jakarta.servlet.http.HttpSession;
import springbootjsp.model.SuDungDichVu;
import springbootjsp.model.ThanhToan;
import springbootjsp.model.User;
import springbootjsp.repository.NhanVienRepository;
import springbootjsp.service.KhachHangService;
import springbootjsp.service.NhanVienService;
import springbootjsp.service.SuDungDichVuService;

@Controller
@RequestMapping("/sddv")
public class SuDungDichVuController {

    private final SuDungDichVuService service;
    private final KhachHangService khService;
    private final NhanVienService nvService; // ✅ thêm
    private final NhanVienRepository nhanVienRepository; // thêm repository

    public SuDungDichVuController(SuDungDichVuService service, KhachHangService khService, NhanVienService nvService, NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
        this.service = service;
        this.khService = khService;
        this.nvService = nvService;
    }

    @GetMapping("/form")
    public String formSDDV(@RequestParam(required = false) String maSDDV,
                           Model model,
                           HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        SuDungDichVu sddv;

        if (maSDDV != null) {
            // ✅ Nếu đang sửa
            sddv = service.getById(maSDDV)
                    .orElseThrow(() -> new RuntimeException("Sử dụng dịch vụ không tồn tại"));
        } else {
            // ✅ Nếu đang thêm mới
            sddv = new SuDungDichVu();
            sddv.setMaSDDV(service.generateNextMaSDDV());
            sddv.setNgaySDDV(LocalDate.now());
            sddv.setTongTien(java.math.BigDecimal.ZERO);
            sddv.setThanhToan(ThanhToan.CHUA_THANH_TOAN);

            // ✅ Lấy mã nhân viên từ session hoặc từ DB
            if (loggedInUser != null) {
                String maNV = null;

                // Nếu trong session có nhân viên
                if (loggedInUser.getNhanVien() != null) {
                    maNV = loggedInUser.getNhanVien().getMaNhanVien();
                }


                if (maNV != null) {
                    var nv = nhanVienRepository.getReferenceById(maNV);
                    sddv.setMaNhanVien(nv);
                }
            }
        }

        // ✅ Gán các danh sách cần thiết cho form
        model.addAttribute("sddv", sddv);
        model.addAttribute("NVList", nvService.getAllNhanVien());
        model.addAttribute("khachHangList", khService.getAllKhachHang());
        model.addAttribute("thanhToanOptions", ThanhToan.values());

        return "sudungdichvu/form-wrapper";
    }


    @InitBinder("kiemTraPhong")
    public void disallowThanhToanKT(WebDataBinder binder) {
        binder.setDisallowedFields("thanhToan");
    }

    @PostMapping("/save")
    public String saveSDDV(@ModelAttribute SuDungDichVu sddv, RedirectAttributes ra) {
        try {
            service.save(sddv);
            ra.addFlashAttribute("message", "Lưu thành công");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            // quay lại form (giữ tham số nếu đang sửa)
            return "redirect:/sddv/form" + (sddv.getMaSDDV()!=null ? "?maSDDV="+sddv.getMaSDDV() : "");
        }
        return "redirect:/sddv";
    }

    @GetMapping("/delete")
    public String deleteSDDV(@RequestParam String maSDDV, RedirectAttributes ra) {
        try {
            service.deleteById(maSDDV);
            ra.addFlashAttribute("message", "Xóa thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/sddv";
    }

    @GetMapping
    public String listSDDV(Model model,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) ThanhToan thanhToan, // ✅ thêm filter theo ThanhToan
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("list", service.search(keyword));
        } else if (startDate != null || endDate != null) {
            model.addAttribute("list", service.filterByDate(startDate, endDate));
        } else {
            model.addAttribute("list", service.getAll());
        }
        // giữ lại giá trị trên view
        model.addAttribute("keyword", keyword);
        model.addAttribute("thanhToan", thanhToan);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("thanhToanOptions", ThanhToan.values());
//        return "sddv-list";
         return "sudungdichvu/list-wrapper";
    }
}
