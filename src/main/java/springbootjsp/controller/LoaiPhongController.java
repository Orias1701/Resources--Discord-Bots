package springbootjsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springbootjsp.model.LoaiPhong;
import springbootjsp.service.LoaiPhongService;

import java.math.BigDecimal;

@Controller
@RequestMapping("/loaiphong")
public class LoaiPhongController {

    private final LoaiPhongService service;

    public LoaiPhongController(LoaiPhongService service) {
        this.service = service;
    }

    // Trang danh sach
    @GetMapping
    public String listLoaiPhong(Model model,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(required = false) BigDecimal minPrice,
                                @RequestParam(required = false) BigDecimal maxPrice) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("list", service.search(keyword));
        } else if (minPrice != null || maxPrice != null) {
            model.addAttribute("list", service.filterByPrice(minPrice, maxPrice));
        } else {
            model.addAttribute("list", service.getAll());
        }
//        return "loaiphong-list";
         return "loaiphong/list-wrapper";
    }

    // Trang form them moi
    @GetMapping("/form")
    public String formLoaiPhong(@RequestParam(required = false) String maLoai, Model model) {
        if (maLoai != null) {
            LoaiPhong lp = service.getById(maLoai)
                    .orElseThrow(() -> new RuntimeException("Loai phong khong ton tai"));
            model.addAttribute("loaiphong", lp);
        } else {
            // Truong hop tao moi: sinh maLoai moi
            LoaiPhong lp = new LoaiPhong();
            lp.setMaLoai(service.generateNextMaLoaiPhong());
            model.addAttribute("loaiphong", lp);
        }
//        return "loaiphong-form";
        return "loaiphong/form-wrapper";

    }


    // Luu (them moi hoac cap nhat)
    @PostMapping("/save")
    public String saveLoaiPhong(@ModelAttribute LoaiPhong loaiphong) {
        service.save(loaiphong);
        return "redirect:/loaiphong";
    }

    // Xoa
    @GetMapping("/delete")
    public String deleteLoaiPhong(@RequestParam String maLoai) {
        service.deleteById(maLoai);
        return "redirect:/loaiphong";
    }
}
