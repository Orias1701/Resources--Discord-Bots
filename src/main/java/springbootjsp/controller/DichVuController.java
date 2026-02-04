package springbootjsp.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springbootjsp.model.DichVu;
import springbootjsp.service.DichVuService;

@Controller
@RequestMapping("/dichvu")
public class DichVuController {

    private final DichVuService service;

    public DichVuController(DichVuService service) {
        this.service = service;
    }

    // Trang danh sách
    @GetMapping
    public String listDichVu(Model model,
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
//        return "dichvu-list";
         return "dichvu/list-wrapper";
    }

    // Trang form thêm/sửa
    @GetMapping("/form")
    public String formDichVu(@RequestParam(required = false) String maDichVu, Model model) {
        if (maDichVu != null) {
            DichVu dv = service.getById(maDichVu)
                    .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại"));
            model.addAttribute("dichvu", dv);
        } else {
            DichVu dv = new DichVu();
            dv.setMaDichVu(service.generateNextMaDichVu());
            model.addAttribute("dichvu", dv);
        }
         return "dichvu/form-wrapper";
//        return "dichvu-form";
    }

    // Lưu (thêm hoặc cập nhật)
    @PostMapping("/save")
    public String saveDichVu(@ModelAttribute DichVu dichvu) {
        service.save(dichvu);
        return "redirect:/dichvu";
    }

    // Xóa
    @GetMapping("/delete")
    public String deleteDichVu(@RequestParam String maDichVu) {
        service.deleteById(maDichVu);
        return "redirect:/dichvu";
    }
}
