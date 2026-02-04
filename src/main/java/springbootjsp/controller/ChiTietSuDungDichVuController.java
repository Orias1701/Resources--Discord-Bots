package springbootjsp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springbootjsp.model.ChiTietSuDungDichVu;
import springbootjsp.model.DichVu;
import springbootjsp.model.SuDungDichVu;
import springbootjsp.service.ChiTietSuDungDichVuService;
import springbootjsp.service.DichVuService;
import springbootjsp.service.SuDungDichVuService;

@Controller
@RequestMapping("/chitietdichvu")
public class ChiTietSuDungDichVuController {

    private final ChiTietSuDungDichVuService ctService;
    private final DichVuService dichVuService;
    private final SuDungDichVuService sddvService;


    // @Autowired
    public ChiTietSuDungDichVuController(ChiTietSuDungDichVuService ctService,
                                         DichVuService dichVuService,
                                         SuDungDichVuService sddvService) {
        this.ctService = ctService;
        this.dichVuService = dichVuService;
        this.sddvService = sddvService;
    }

    @GetMapping
    public String listChiTietDichVu(Model model,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Integer minSL,
                                    @RequestParam(required = false) Integer maxSL) {

        List<ChiTietSuDungDichVu> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = ctService.searchByKeyword(keyword);
        } else if (minSL != null || maxSL != null) {
            list = ctService.filterBySoLuong(minSL, maxSL);
        } else {
            list = ctService.getAll();
        }

        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minSL", minSL);
        model.addAttribute("maxSL", maxSL);
//        return "chitietdichvu-list";
         return "chitietdichvu/list-wrapper";

    }

    @GetMapping("/form")
    public String formChiTietDichVu(@RequestParam(required = false) Long id,
                                    Model model) {
        ChiTietSuDungDichVu ct;
        if (id != null) {
            ct = ctService.getById(id)
                    .orElseThrow(() -> new RuntimeException("Chi tiet khong ton tai"));
        } else {
            ct = new ChiTietSuDungDichVu();
        }

        List<DichVu> listDV = dichVuService.getAll();
        List<SuDungDichVu> listSDDV = sddvService.getAll();

        model.addAttribute("ct", ct);
        model.addAttribute("listDV", listDV);
        model.addAttribute("listSDDV", listSDDV);

         return "chitietdichvu/form-wrapper";
//        return "chitietdichvu-form";
    }

    @PostMapping("/save")
    public String saveChiTietDichVu(@RequestParam(required=false) Long id,
                                    @RequestParam Integer soLuong,
                                    @RequestParam String maDichVu,
                                    @RequestParam String maSDDV) {
        ChiTietSuDungDichVu ct = (id != null)
                ? ctService.getById(id).orElse(new ChiTietSuDungDichVu())
                : new ChiTietSuDungDichVu();

        ct.setSoLuong(soLuong);

        DichVu dv = new DichVu();
        dv.setMaDichVu(maDichVu);
        ct.setDichVu(dv);

        SuDungDichVu sddv = new SuDungDichVu();
        sddv.setMaSDDV(maSDDV);
        ct.setSuDungDichVu(sddv);

        ctService.save(ct);
        return "redirect:/chitietdichvu";
    }

    @GetMapping("/delete")
    public String deleteChiTietDichVu(@RequestParam Long id) {
        ctService.delete(id);
        return "redirect:/chitietdichvu";
    }
}