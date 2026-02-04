package springbootjsp.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import springbootjsp.model.User;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Lấy user thật từ CustomUserDetails
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        // ✅ Lưu vào session để dùng cho JSP hoặc controller
        HttpSession session = request.getSession();
        session.setAttribute("loggedInUser", user);

        System.out.println("🔹 Đã lưu session loggedInUser = " + user.getUsername()
                + " (NV: " + (user.getNhanVien() != null ? user.getNhanVien().getMaNhanVien() : "null") + ")");

        // Chuyển hướng sau khi login
        response.sendRedirect("/phong");
    }
}
