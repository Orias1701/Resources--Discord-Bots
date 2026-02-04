package springbootjsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springbootjsp.model.Phong;
import springbootjsp.service.LoaiPhongService;
import springbootjsp.service.PhongService;

import java.util.List;

@Controller
@RequestMapping("/phong")
public class PhongController {

    private final PhongService phongService;
    private final LoaiPhongService loaiPhongService;

    public PhongController(PhongService phongService, LoaiPhongService loaiPhongService) {
        this.phongService = phongService;
        this.loaiPhongService = loaiPhongService;
    }

    @GetMapping
    public String listPhong(Model model,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) Phong.TinhTrangPhong tinhTrang) {

        List<Phong> list;
        if (keyword != null && !keyword.isEmpty()) {
            list = phongService.search(keyword);
        } else if (tinhTrang != null) {
            list = phongService.filterByTinhTrang(tinhTrang);
        } else {
            list = phongService.getAll();
        }
        model.addAttribute("list", list);
        model.addAttribute("tinhTrangOptions", Phong.TinhTrangPhong.values());
//        return "phong-list";
         return "phong/list-wrapper";
    }

    @GetMapping("/form")
    public String formPhong(@RequestParam(required = false) String maPhong, Model model) {
        if (maPhong != null) {
            Phong p = phongService.getById(maPhong)
                    .orElseThrow(() -> new RuntimeException("Phong khong ton tai"));
            model.addAttribute("phong", p);
        } else {
            Phong p = new Phong();
            p.setMaPhong(phongService.generateNextMaPhong());
            model.addAttribute("phong", p);
        }
        // danh sach loai phong de select
        model.addAttribute("listLoaiPhong", loaiPhongService.getAll());
        // danh sach tinh trang de select
        model.addAttribute("tinhTrangOptions", Phong.TinhTrangPhong.values());
//        return "phong-form";
        return "phong/form-wrapper";

    }


    @PostMapping("/save")
    public String savePhong(@ModelAttribute Phong phong) {
        phongService.save(phong);
        return "redirect:/phong";
    }

    @GetMapping("/delete")
    public String deletePhong(@RequestParam String maPhong) {
        phongService.deleteById(maPhong);
        return "redirect:/phong";
    }

    @PostMapping("/updateTinhTrang")
    @ResponseBody
    public String updateTinhTrang(@RequestParam String maPhong,
                                  @RequestParam Phong.TinhTrangPhong tinhTrangPhong) {
        Phong p = phongService.getById(maPhong)
                .orElseThrow(() -> new RuntimeException("Phong khong ton tai"));
        p.setTinhTrangPhong(tinhTrangPhong);
        phongService.save(p);
        return "OK";
    }

}
