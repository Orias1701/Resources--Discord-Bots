package springbootjsp.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springbootjsp.model.HoaDonChiTiet;
import springbootjsp.model.ThanhToan;
import springbootjsp.repository.DatPhongRepository;
import springbootjsp.repository.HoaDonRepository;
import springbootjsp.repository.KiemTraPhongRepository;
import springbootjsp.repository.SuDungDichVuRepository;
import springbootjsp.service.HoaDonChiTietService;
import springbootjsp.service.HoaDonService;
import springbootjsp.service.KiemTraPhongService;

@Controller
@RequestMapping("/hdct")
public class HoaDonChiTietController {

    private final HoaDonChiTietService service;
    private final springbootjsp.repository.HoaDonChiTietRepository repository;

    private final HoaDonRepository hoaDonRepository;
    private final DatPhongRepository datPhongRepository;
    private final SuDungDichVuRepository sddvRepository;
    private final KiemTraPhongRepository ktRepository;
    private final HoaDonService hoaDonService ;
    

    private final KiemTraPhongService kiemTraPhongService;

    public HoaDonChiTietController(HoaDonChiTietService service,
                                   springbootjsp.repository.HoaDonChiTietRepository repository,
                                   HoaDonRepository hoaDonRepository,
                                   DatPhongRepository datPhongRepository,
                                   SuDungDichVuRepository sddvRepository,
                                   KiemTraPhongRepository ktRepository,
                                   KiemTraPhongService kiemTraPhongService,
                                   HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
        this.service = service;
        this.repository = repository;
        this.hoaDonRepository = hoaDonRepository;
        this.datPhongRepository = datPhongRepository;
        this.sddvRepository = sddvRepository;
        this.ktRepository= ktRepository;
        this.kiemTraPhongService = kiemTraPhongService;
    }

    // Cung cấp danh sách enum cho mọi view của controller
    @ModelAttribute("thanhToanOptions")
    public ThanhToan[] thanhToanOptions() {
        return ThanhToan.values();
    }

    @GetMapping
    public String list(@RequestParam(required = false) String maHoaDon, Model model) {
        List<HoaDonChiTiet> list = (maHoaDon == null || maHoaDon.isBlank())
                ? repository.findAllWithDetails()
                : repository.findHoaDonChiTietByMaHoaDonWithDetails(maHoaDon);
        model.addAttribute("list", list);
        model.addAttribute("maHoaDon", maHoaDon);
//        return "hdct-list";
         return "hoadonchitiet/list-wrapper";
    }

    @GetMapping("/form")
    public String form(@RequestParam(required = false) Long id,
                    @RequestParam(required = false) String maHoaDon,
                    Model model) {

        HoaDonChiTiet hdct = (id == null)
                ? new HoaDonChiTiet()
                : service.getById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy HDCT id=" + id));

        if (hdct.getThanhToan() == null) hdct.setThanhToan(ThanhToan.CHUA_THANH_TOAN);

        // Gán sẵn mã HĐ nếu mở từ màn hoá đơn
        if (maHoaDon != null && !maHoaDon.isBlank()
                && (hdct.getHoaDon() == null || hdct.getHoaDon().getMaHoaDon() == null)) {
            var hd = new springbootjsp.model.HoaDon();
            hd.setMaHoaDon(maHoaDon);
            hdct.setHoaDon(hd);
        }

        // ✨ Lấy mã HĐ hiệu lực để lọc list
        String effectiveMaHd = maHoaDon;
        if ((effectiveMaHd == null || effectiveMaHd.isBlank())
                && hdct.getHoaDon() != null && hdct.getHoaDon().getMaHoaDon() != null) {
            effectiveMaHd = hdct.getHoaDon().getMaHoaDon();
        }

        HoaDonService.EligibleChildren eligible = null;
        if (effectiveMaHd != null && !effectiveMaHd.isBlank()) {
            eligible = hoaDonService.getEligibleChildrenForInvoice(effectiveMaHd);
        }

        model.addAttribute("hdct", hdct);
        model.addAttribute("hoaDonList", hoaDonRepository.findAll());
        model.addAttribute("eligible", eligible);
        model.addAttribute("datPhongList", eligible != null ? eligible.getDatPhongOptions() : java.util.List.of());
        model.addAttribute("sddvList",     eligible != null ? eligible.getSddvOptions()     : java.util.List.of());

        return "hoadonchitiet/form-wrapper";
    }



    // API JSON cho AJAX khi đổi Hóa đơn ở form HDCT
    @GetMapping(value = "/api/eligible-children", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public HoaDonService.EligibleChildren eligibleChildren(@RequestParam String maHoaDon) {
        return hoaDonService.getEligibleChildrenForInvoice(maHoaDon);
    }

    @GetMapping("/api/get-tien-kiem-tra")
    @ResponseBody
    public BigDecimal getTienKiemTra(@RequestParam(required = false) String maKiemTra) {
        if (maKiemTra == null || maKiemTra.isBlank()) return BigDecimal.ZERO;
        BigDecimal v = kiemTraPhongService.getTienKiemTraByMaKiemTra(maKiemTra);
        return v != null ? v : BigDecimal.ZERO;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute HoaDonChiTiet hdct, RedirectAttributes ra) {
        try {
            // HÓA ĐƠN
            if (hdct.getHoaDon() != null
                    && hdct.getHoaDon().getMaHoaDon() != null
                    && !hdct.getHoaDon().getMaHoaDon().isBlank()) {
                hdct.setHoaDon(hoaDonRepository.getReferenceById(hdct.getHoaDon().getMaHoaDon()));
            } else {
                hdct.setHoaDon(null);
            }

            // ĐẶT PHÒNG
            if (hdct.getDatPhong() != null
                    && hdct.getDatPhong().getMaDatPhong() != null
                    && !hdct.getDatPhong().getMaDatPhong().isBlank()) {
                hdct.setDatPhong(datPhongRepository.getReferenceById(hdct.getDatPhong().getMaDatPhong()));
            } else {
                hdct.setDatPhong(null);
            }

            // SỬ DỤNG DỊCH VỤ
            if (hdct.getSuDungDichVu() != null
                    && hdct.getSuDungDichVu().getMaSDDV() != null
                    && !hdct.getSuDungDichVu().getMaSDDV().isBlank()) {
                hdct.setSuDungDichVu(sddvRepository.getReferenceById(hdct.getSuDungDichVu().getMaSDDV()));
            } else {
                hdct.setSuDungDichVu(null);
            }

            // KIỂM TRA PHÒNG
            if (hdct.getKiemTraPhong() != null
                    && hdct.getKiemTraPhong().getMaKiemTra() != null
                    && !hdct.getKiemTraPhong().getMaKiemTra().isBlank()) {
                hdct.setKiemTraPhong(ktRepository.getReferenceById(hdct.getKiemTraPhong().getMaKiemTra()));
            } else {
                hdct.setKiemTraPhong(null);
            }

            // MẶC ĐỊNH TRẠNG THÁI TRƯỚC KHI LƯU (nếu người dùng không chọn)
            if (hdct.getThanhToan() == null) {
                hdct.setThanhToan(ThanhToan.CHUA_THANH_TOAN);
            }

            service.save(hdct);
            ra.addFlashAttribute("message", "Thêm/Cập nhật hóa đơn chi tiết thành công");
        } catch (jakarta.persistence.EntityNotFoundException ex) {
            ra.addFlashAttribute("error", "Một mã liên kết không tồn tại: " + ex.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi lưu Hóa đơn chi tiết: " + e.getMessage());
        }
        return "redirect:/hdct";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("message", "Xóa thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/hdct";
    }

    // Đánh dấu ĐÃ THANH TOÁN
    @PostMapping("/mark-paid")
    public String markPaid(@RequestParam Long id, RedirectAttributes ra) {
        try {
            service.setHdctThanhToan(id, ThanhToan.DA_THANH_TOAN);
            ra.addFlashAttribute("message", "HDCT đã chuyển sang ĐÃ THANH TOÁN");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/hdct";
    }

    // Đánh dấu CHƯA THANH TOÁN
    @PostMapping("/mark-unpaid")
    public String markUnpaid(@RequestParam Long id, RedirectAttributes ra) {
        try {
            service.setHdctThanhToan(id, ThanhToan.CHUA_THANH_TOAN);
            ra.addFlashAttribute("message", "HDCT đã chuyển sang CHƯA THANH TOÁN");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/hdct";
    }
    @GetMapping(value = "/api/preview", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, BigDecimal> previewTotals(
            @RequestParam(required = false) String maDatPhong,
            @RequestParam(required = false) String maSDDV,
            @RequestParam(required = false) String maKiemTra) {

        var dto = service.quickPreview(maDatPhong, maSDDV, maKiemTra);
        return Map.of(
            "tienDatPhong", dto.getTienDatPhong(),
            "tienSddv",     dto.getTienSddv(),
            "tienKiemTra",  dto.getTienKiemTra(),
            "thanhTien",    dto.getThanhTien()
        );
    }
    
}