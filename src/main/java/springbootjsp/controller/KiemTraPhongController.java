package springbootjsp.controller;

// import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springbootjsp.model.*;
import springbootjsp.service.DatPhongService;
import springbootjsp.service.KiemTraPhongService;
import springbootjsp.service.NhanVienService;
import springbootjsp.service.PhongService;

@Controller
@RequestMapping("/kiemtraphong")
public class KiemTraPhongController {

    private final KiemTraPhongService service;
    private final PhongService phongService;
    private final NhanVienService nhanVienService;
    private final DatPhongService datPhongService;

    public KiemTraPhongController(KiemTraPhongService service, PhongService phongService, NhanVienService nhanVienService, DatPhongService datPhongService) {
        this.datPhongService = datPhongService;
        this.service = service;
        this.phongService = phongService;
        this.nhanVienService = nhanVienService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) KiemTraPhong.TrangThai trangThai) {

        List<KiemTraPhong> list;
        if (keyword != null && !keyword.isEmpty()) {
            list = service.search(keyword);
        } else if (trangThai != null) {
            list = service.filterByTrangThai(trangThai);
        } else {
            list = service.getAll();
        }
        model.addAttribute("list", list);
        model.addAttribute("trangThaiOptions", KiemTraPhong.TrangThai.values());
//        return "kiemtraphong-list";
         return "kiemtraphong/list-wrapper";
    }

    @GetMapping("/form")
    public String form(@RequestParam(required = false) String maKiemTra, Model model, HttpSession session) {

        List<Phong> phongList = phongService.getALL();

        // 🔹 Lấy user hiện tại từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // 1️⃣ Tạo hoặc lấy kiểm tra phòng
        KiemTraPhong ktp;
        if (maKiemTra != null && !maKiemTra.isBlank()) {
            ktp = service.getById(maKiemTra)
                    .orElseThrow(() -> new RuntimeException("Không tồn tại kiểm tra phòng"));
        } else {
            ktp = new KiemTraPhong();
            ktp.setMaKiemTra(service.generateNextMaKiemTra());
            ktp.setThanhToan(ThanhToan.CHUA_THANH_TOAN);
            ktp.setNgayKiemTra(LocalDateTime.now());
            ktp.setTienKiemTra(java.math.BigDecimal.ZERO);

            // ✅ Gán nhân viên đăng nhập
            if (loggedInUser != null && loggedInUser.getNhanVien() != null) {
                ktp.setMaNhanVien(loggedInUser.getNhanVien());
            }
        }

        // 2️⃣ Lọc danh sách đặt phòng hợp lệ
        List<DatPhong> datPhongList = new ArrayList<>();
        for (DatPhong dp : datPhongService.findAll()) {
            if (dp.getThanhToan() == ThanhToan.CHUA_THANH_TOAN
                    && dp.getTinhTrang() != DatPhong.TinhTrang.DANG_DOI) {
                datPhongList.add(dp);
            }
        }

        // 3️⃣ Gán dữ liệu cho view
        model.addAttribute("kiemtraphong", ktp);
        model.addAttribute("listDatPhong", datPhongList);
        model.addAttribute("listPhong", phongList);

        // 🧩 Thêm nhân viên đăng nhập để hiển thị
        if (loggedInUser != null && loggedInUser.getNhanVien() != null) {
            model.addAttribute("nhanVienDangNhap", loggedInUser.getNhanVien());
        }

        model.addAttribute("trangThaiOptions", KiemTraPhong.TrangThai.values());
        return "kiemtraphong/form-wrapper";
    }


    // @InitBinder("kiemTraPhong")
    // public void disallowThanhToanKT(WebDataBinder binder) {
    //     binder.setDisallowedFields("thanhToan");
    // }
    @PostMapping("/save")
    public String save(@ModelAttribute KiemTraPhong ktp,
                       HttpSession session,
                       RedirectAttributes ra) {
        try {
            // ✅ Nếu ngày kiểm tra chưa có, gán mặc định là hiện tại
            if (ktp.getNgayKiemTra() == null) {
                ktp.setNgayKiemTra(LocalDateTime.now());
            }

            // ✅ Gán nhân viên đang đăng nhập
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser != null && loggedInUser.getNhanVien() != null) {
                ktp.setMaNhanVien(loggedInUser.getNhanVien());
            }

            // ✅ Lưu vào DB
            service.save(ktp);
            ra.addFlashAttribute("message", "Lưu kiểm tra phòng thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi lưu kiểm tra phòng: " + e.getMessage());
        }

        return "redirect:/kiemtraphong";
    }


    @GetMapping("/delete")
    public String delete(@RequestParam String maKiemTra) {
        service.deleteById(maKiemTra);
        return "redirect:/kiemtraphong";
    }
    // ================== API TRA CỨU PHÒNG TỪ MÃ ĐẶT PHÒNG ==================

    public static record RoomDto(String maPhong, String tenPhong) {}

    /** GET /kiemtraphong/room-by-dp?maDatPhong=DP001
     *  -> { "maPhong": "P001", "tenPhong": "Phòng 101" } (ví dụ)
     */
        @GetMapping("/room-by-dp")
        @ResponseBody
        public RoomDto getRoomByBooking(@RequestParam String maDatPhong) {
            var dp = datPhongService.findById(maDatPhong);
            if (dp == null || dp.getMaPhong() == null) {
                return new RoomDto(null, null);
            }
            var p = dp.getMaPhong();
            return new RoomDto(p.getMaPhong(), p.getTenPhong());
        }
}