package springbootjsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springbootjsp.model.ThietBi;
import springbootjsp.service.ThietBiService;

import java.util.List;

@Controller
@RequestMapping("/thietbi")
public class ThietBiController {

    private final ThietBiService service;

    public ThietBiController(ThietBiService service) {
        this.service = service;
    }

    // List + tim kiem gop
    @GetMapping
    public String listThietBi(Model model,
                              @RequestParam(required = false) String keyword) {

        List<ThietBi> list = (keyword == null || keyword.isEmpty())
                ? service.getAll()
                : service.searchByKeyword(keyword);

        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);
        // return "thietbi-list";
        return "thietbi/list-wrapper";
    }

    // Form them/sua
    @GetMapping("/form")
    public String formThietBi(@RequestParam(required = false) String maThietBi,
                              Model model) {

        ThietBi tb;
        if (maThietBi != null) {
            tb = service.getById(maThietBi)
                    .orElseThrow(() -> new RuntimeException("Thiet bi khong ton tai"));
        } else {
            tb = new ThietBi();
            tb.setMaThietBi(service.generateNextMaThietBi()); // tao ma moi
        }

        model.addAttribute("tb", tb); // chi can thuoc tinh "tb"
        return "thietbi/form-wrapper";
    }


    // Save
    @PostMapping("/save")
    public String saveThietBi(@ModelAttribute ThietBi tb) {
        service.save(tb);
        return "redirect:/thietbi";
    }

    // Delete
    @GetMapping("/delete")
    public String deleteThietBi(@RequestParam String maThietBi) {
        service.delete(maThietBi);
        return "redirect:/thietbi";
    }
}
