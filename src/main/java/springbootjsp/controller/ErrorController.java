package springbootjsp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/errorPage")
    public String showErrorPage() {
        return "errorPage";  // Đảm bảo rằng file JSP này tồn tại
    }
}
