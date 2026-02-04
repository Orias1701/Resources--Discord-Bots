package springbootjsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springbootjsp.model.ThietBiPhong;
import springbootjsp.model.ThietBi;
import springbootjsp.model.Phong;
import springbootjsp.service.ThietBiPhongService;
import springbootjsp.service.PhongService;
// Nếu bạn có ThietBiService, dùng nó; nếu không có, hãy đổi sang repository tương ứng
import springbootjsp.service.ThietBiService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/thietbiphong")
public class ThietBiPhongController {

    private final ThietBiPhongService tbpService;
    private final PhongService phongService;
    private final ThietBiService thietBiService;

    public ThietBiPhongController(ThietBiPhongService tbpService,
                                  PhongService phongService,
                                  ThietBiService thietBiService) {
        this.tbpService = tbpService;
        this.phongService = phongService;
        this.thietBiService = thietBiService;
    }

    // ======= LIST + SEARCH =======
    @GetMapping({"", "/", "/list"})
    public String list(@RequestParam(value = "q", required = false) String keyword,
                       @RequestParam(value = "trangThai", required = false) ThietBiPhong.TrangThai trangThai,
                       Model model) {
        var items = tbpService.search(keyword, trangThai);
        model.addAttribute("items", items);
        model.addAttribute("q", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("allTrangThais", ThietBiPhong.TrangThai.values());
//        return "thietbiphong-list";
         return "thietbiphong/list-wrapper";
    }

    // ======= CREATE FORM =======
    @GetMapping("/new")
    public String createForm(Model model) {
        ThietBiPhong tbp = new ThietBiPhong();
        prepareFormModel(model, tbp, null, null);
        return "thietbiphong/form-wrapper";
    }

    // ======= EDIT FORM theo cặp (MaPhong, MaThietBi) để giữ nguyên cách dùng cũ =======
    @GetMapping("/edit")
    public String editFormByPair(@RequestParam("maPhong") String maPhong,
                                 @RequestParam("maThietBi") String maThietBi,
                                 Model model,
                                 RedirectAttributes ra) {
        Optional<ThietBiPhong> opt = tbpService.getById(maPhong, maThietBi);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("error", "Không tìm thấy bản ghi: MaPhong=" + maPhong + ", MaThietBi=" + maThietBi);
            return "redirect:/thietbiphong";
        }
        ThietBiPhong tbp = opt.get();
        prepareFormModel(model, tbp, maPhong, maThietBi); // truyền old pair để form mang theo
        return "thietbiphong/form-wrapper";
    }

    // ======= SAVE =======
    @PostMapping("/save")
    public String save(@RequestParam("maPhong") String maPhong,
                       @RequestParam("maThietBi") String maThietBi,
                       @RequestParam(value = "soLuong", required = false) Integer soLuong,
                       @RequestParam("trangThai") ThietBiPhong.TrangThai trangThai,
                       @RequestParam(value = "oldMaPhong", required = false) String oldMaPhong,
                       @RequestParam(value = "oldMaThietBi", required = false) String oldMaThietBi,
                       RedirectAttributes ra) {
        try {
            // Tạo entity
            ThietBiPhong tbp = new ThietBiPhong();
            Phong p = new Phong(); p.setMaPhong(maPhong);
            ThietBi t = new ThietBi(); t.setMaThietBi(maThietBi);

            tbp.setPhong(p);
            tbp.setThietBi(t);
            tbp.setSoLuong(soLuong);
            tbp.setTrangThai(trangThai);

            // Cảnh báo nếu thay đổi cặp khóa (khi edit)
            boolean changingPair = (oldMaPhong != null && oldMaThietBi != null)
                    && (!oldMaPhong.equals(maPhong) || !oldMaThietBi.equals(maThietBi));

            // Kiểm tra tồn tại theo cặp mới
            Optional<ThietBiPhong> existedNewPair = tbpService.getById(maPhong, maThietBi);
            if (existedNewPair.isPresent() && changingPair) {
                ra.addFlashAttribute("warn",
                        "Cặp (MaPhong, MaThietBi) mới đã tồn tại. Hệ thống sẽ cập nhật bản ghi hiện có.");
            } else if (existedNewPair.isPresent()) {
                // đang cập nhật chính bản ghi cũ (cùng cặp)
                ra.addFlashAttribute("success", "Cập nhật thành công!");
            } else if (changingPair) {
                // chuyển sang cặp mới chưa tồn tại
                ra.addFlashAttribute("success", "Đã cập nhật sang cặp (MaPhong, MaThietBi) mới.");
            }

            // Lưu (service sẽ tự map id cũ nếu trùng cặp để tránh UNIQUE)
            tbpService.save(tbp);

            if (!existedNewPair.isPresent() && !changingPair) {
                ra.addFlashAttribute("success", "Thêm mới thành công!");
            }
            return "redirect:/thietbiphong";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể lưu: " + e.getMessage());
            return "redirect:/thietbiphong";
        }
    }

    // ======= DELETE theo cặp để giữ nguyên cách gọi =======
    @GetMapping("/delete")
    public String delete(@RequestParam("maPhong") String maPhong,
                         @RequestParam("maThietBi") String maThietBi,
                         RedirectAttributes ra) {
        try {
            Optional<ThietBiPhong> existed = tbpService.getById(maPhong, maThietBi);
            if (existed.isEmpty()) {
                ra.addFlashAttribute("warn", "Bản ghi không tồn tại (đã xoá trước đó?).");
                return "redirect:/thietbiphong";
            }
            tbpService.delete(maPhong, maThietBi);
            ra.addFlashAttribute("success", "Đã xoá: MaPhong=" + maPhong + ", MaThietBi=" + maThietBi);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xoá: " + e.getMessage());
        }
        return "redirect:/thietbiphong";
    }


    // ======= Helpers =======
    private void prepareFormModel(Model model, ThietBiPhong tbp, String oldMaPhong, String oldMaThietBi) {
        List<Phong> listPhong = phongService.getAll();      // dùng service có sẵn
        List<ThietBi> listTB = thietBiService.getAll();     // giả định có service này

        model.addAttribute("tbp", tbp);
        model.addAttribute("listPhong", listPhong);
        model.addAttribute("listThietBi", listTB);
        model.addAttribute("allTrangThais", ThietBiPhong.TrangThai.values());

        // mang theo cặp cũ (nếu edit)
        model.addAttribute("oldMaPhong", oldMaPhong != null ? oldMaPhong :
                (tbp.getPhong() != null ? tbp.getPhong().getMaPhong() : null));
        model.addAttribute("oldMaThietBi", oldMaThietBi != null ? oldMaThietBi :
                (tbp.getThietBi() != null ? tbp.getThietBi().getMaThietBi() : null));
    }
}
