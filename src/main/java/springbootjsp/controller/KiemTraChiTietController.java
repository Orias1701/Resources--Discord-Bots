package springbootjsp.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import springbootjsp.model.KiemTraChiTiet;
import springbootjsp.model.KiemTraPhong;
import springbootjsp.service.KiemTraChiTietService;

@Controller
@RequestMapping("/kiemtrachitiet")
public class KiemTraChiTietController {

    @Autowired private KiemTraChiTietService service;

    // ĐÃ SỬA: Bỏ "/list" để đường dẫn trở thành /kiemtrachitiet
    @GetMapping
    public String showListPage(Model model,
                               @RequestParam(name = "keyword", required = false) String keyword,
                               @RequestParam(name = "tinhTrang", required = false) String tinhTrang) {

        List<KiemTraChiTiet> kiemTraChiTiets;

        if (keyword != null && !keyword.trim().isEmpty()) {
            kiemTraChiTiets = service.timKiemTheoTuKhoa(keyword);
        } else if (tinhTrang != null && !tinhTrang.trim().isEmpty()) {
            // ĐÃ SỬA: Gọi phương thức lọc theo tình trạng
            kiemTraChiTiets = service.locTheoTinhTrang(tinhTrang);
        } else {
            kiemTraChiTiets = service.layTatCaKiemTraChiTiet();
        }

        model.addAttribute("list", kiemTraChiTiets);
        model.addAttribute("keyword", keyword);
        // ĐÃ SỬA: Thêm tham số tình trạng vào model
        model.addAttribute("tinhTrang", tinhTrang);

//        return "list_kiemtrachitiet";
        return "kiemtrachitiet/list-wrapper";

    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<KiemTraPhong> kiemTraPhongs = service.layTatCaKiemTraPhong();
        model.addAttribute("kiemTraPhongs", kiemTraPhongs);
        return "kiemtrachitiet/form-wrapper";
    }

    @PostMapping("/add")
    public String addKiemTraChiTiet(@RequestParam("maKiemTra") String maKiemTra,
                                    @RequestParam("maThietBi") String maThietBi,
                                    @RequestParam("tinhTrang") String tinhTrang,
                                    @RequestParam("soLuongHong") int soLuongHong,
                                    @RequestParam("ghiChu") String ghiChu,
                                    Model model) { // THÊM Model vào tham số
        try {
            service.luuKiemTraChiTiet(maKiemTra, maThietBi, tinhTrang, soLuongHong, ghiChu);
            return "redirect:/kiemtrachitiet?success";
        } catch (IllegalArgumentException e) {
            // Bắt lỗi và thêm thông báo lỗi vào model
            model.addAttribute("errorMessage", e.getMessage());

            // Cần thêm lại dữ liệu cho dropdown để form không bị trống
            List<KiemTraPhong> kiemTraPhongs = service.layTatCaKiemTraPhong();
            model.addAttribute("kiemTraPhongs", kiemTraPhongs);

            // Trả về lại form add để hiển thị lỗi
            return "kiemtrachitiet/form-wrapper";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("maKiemTra") String maKiemTra,
                               @RequestParam("maThietBi") String maThietBi,
                               Model model) {
        KiemTraChiTiet.KiemTraChiTietId id = new KiemTraChiTiet.KiemTraChiTietId(maKiemTra, maThietBi);
        Optional<KiemTraChiTiet> recordOpt = service.getById(id);

        if (recordOpt.isPresent()) {
            model.addAttribute("kiemTraChiTiet", recordOpt.get());
            return "kiemtrachitiet/form-wrapper";
        } else {
            return "redirect:/kiemtrachitiet?error=notfound";
        }
    }

    @PostMapping("/update")
    public String updateKiemTraChiTiet(@RequestParam("maKiemTra") String maKiemTra,
                                       @RequestParam("maThietBi") String maThietBi,
                                       @RequestParam("tinhTrang") String tinhTrang,
                                       @RequestParam("soLuongHong") int soLuongHong,
                                       @RequestParam("ghiChu") String ghiChu,
                                       Model model) { // THÊM Model vào tham số
        try {
            KiemTraChiTiet.KiemTraChiTietId id = new KiemTraChiTiet.KiemTraChiTietId(maKiemTra, maThietBi);
            service.suaKiemTraChiTiet(id, tinhTrang, soLuongHong, ghiChu);
            return "redirect:/kiemtrachitiet?success=updated";
        } catch (IllegalArgumentException e) {
            // Bắt lỗi và thêm thông báo lỗi vào model
            model.addAttribute("errorMessage", e.getMessage());

            // Cần tải lại dữ liệu của đối tượng để form không bị trống
            KiemTraChiTiet.KiemTraChiTietId id = new KiemTraChiTiet.KiemTraChiTietId(maKiemTra, maThietBi);
            Optional<KiemTraChiTiet> recordOpt = service.getById(id);
            if (recordOpt.isPresent()) {
                model.addAttribute("kiemTraChiTiet", recordOpt.get());
            }

            // Trả về lại form edit để hiển thị lỗi
            return "kiemtrachitiet/form-wrapper";
        }
    }

    @GetMapping("/delete")
    public String deleteKiemTraChiTiet(@RequestParam("maKiemTra") String maKiemTra,
                                       @RequestParam("maThietBi") String maThietBi) {
        KiemTraChiTiet.KiemTraChiTietId id = new KiemTraChiTiet.KiemTraChiTietId(maKiemTra, maThietBi);
        service.xoaKiemTraChiTiet(id);
        return "redirect:/kiemtrachitiet?success=deleted";
    }

    @GetMapping("/api/get-thong-tin-theo-kiem-tra")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getThongTinTheoKiemTra(@RequestParam String maKiemTra) {
        String maPhong = service.layKiemTraPhongBangMa(maKiemTra)
                .map(kp -> kp.getPhong().getMaPhong())
                .orElse(null);
        List<String> maThietBiList = service.layMaThietBiTheoMaPhong(maPhong);
        return ResponseEntity.ok(Map.of("maPhong", maPhong, "maThietBiList", maThietBiList));
    }

    @GetMapping("/api/get-denbu")
    @ResponseBody
    public ResponseEntity<BigDecimal> getDenBu(@RequestParam String maThietBi) {
        BigDecimal denBu = service.layDenBuTheoMaThietBi(maThietBi);
        return ResponseEntity.ok(denBu);
    }
}