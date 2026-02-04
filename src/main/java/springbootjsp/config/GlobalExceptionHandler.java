package springbootjsp.config;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice // bắt cả @Controller và @RestController
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Bật/ẩn chi tiết lỗi cho người dùng (đặt true trong application-dev.properties nếu muốn)
    @Value("${app.errors.expose-details:false}")
    private boolean exposeDetails;

    /* =========================================================
     * 1) LỖI NGHIỆP VỤ / HỢP LỆ HÓA (400)
     * ========================================================= */

    // Lỗi bạn chủ động ném ra trong code business
    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
    public Object handleBusinessErrors(RuntimeException ex,
                                       HttpServletRequest req,
                                       RedirectAttributes ra) {
        return renderError(ex, req, ra, 400, safeMessage("Yêu cầu không hợp lệ", ex));
    }

    // @Valid trên @RequestBody (REST) hoặc form object (MVC)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                               HttpServletRequest req,
                                               RedirectAttributes ra) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + messageOf(fe))
                .collect(Collectors.joining("; "));
        String msg = "Dữ liệu không hợp lệ: " + (detail.isBlank() ? "Vui lòng kiểm tra lại." : detail);
        return renderError(ex, req, ra, 400, msg);
    }

    // @Valid với form-binding (không phải @RequestBody)
    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException ex,
                                      HttpServletRequest req,
                                      RedirectAttributes ra) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + messageOf(fe))
                .collect(Collectors.joining("; "));
        String msg = "Dữ liệu không hợp lệ: " + (detail.isBlank() ? "Vui lòng kiểm tra lại." : detail);
        return renderError(ex, req, ra, 400, msg);
    }

    // @Validated trên @RequestParam / @PathVariable
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolation(ConstraintViolationException ex,
                                            HttpServletRequest req,
                                            RedirectAttributes ra) {
        String detail = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        String msg = "Dữ liệu không hợp lệ: " + (detail.isBlank() ? "Vui lòng kiểm tra lại." : detail);
        return renderError(ex, req, ra, 400, msg);
    }

    // Sai kiểu tham số (?id=abc nhưng id là Long, v.v.)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                     HttpServletRequest req,
                                     RedirectAttributes ra) {
        String msg = "Kiểu dữ liệu không đúng cho tham số '" + ex.getName() + "'.";
        return renderError(ex, req, ra, 400, msg);
    }

    // Body JSON/XML parse fail
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleNotReadable(HttpMessageNotReadableException ex,
                                    HttpServletRequest req,
                                    RedirectAttributes ra) {
        String msg = "Không đọc được nội dung yêu cầu. Vui lòng kiểm tra định dạng dữ liệu.";
        return renderError(ex, req, ra, 400, msg);
    }

    /* =========================================================
     * 2) LỖI DỮ LIỆU / DB (409)
     * ========================================================= */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDataIntegrity(DataIntegrityViolationException ex,
                                      HttpServletRequest req,
                                      RedirectAttributes ra) {
        String msg = "Dữ liệu không hợp lệ hoặc trùng lặp. Vui lòng kiểm tra ràng buộc.";
        return renderError(ex, req, ra, 409, msg);
    }

    /* =========================================================
     * 3) 404 KHÔNG TÌM THẤY
     *  (Cần bật: spring.mvc.throw-exception-if-no-handler-found=true)
     * ========================================================= */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNotFound(NoHandlerFoundException ex,
                                 HttpServletRequest req,
                                 RedirectAttributes ra) {
        String msg = "Không tìm thấy tài nguyên.";
        return renderError(ex, req, ra, 404, msg);
    }

    /* =========================================================
     * 4) MỌI LỖI CÒN LẠI (500)
     * ========================================================= */
    @ExceptionHandler(Exception.class)
    public Object handleAll(Exception ex,
                            HttpServletRequest req,
                            RedirectAttributes ra) {
        String msg = "Có lỗi xảy ra trong hệ thống.";
        return renderError(ex, req, ra, 500, msg);
    }

    /* =========================================================
     * UTIL CHUNG
     * - Ghi log kèm errorId
     * - Phân biệt AJAX vs redirect
     * ========================================================= */
    private Object renderError(Exception ex,
                           HttpServletRequest req,
                           RedirectAttributes ra,
                           int status,
                           String userMessage) {

    String errorId = UUID.randomUUID().toString();
    String path = req.getRequestURI();
    log.error("[errorId={}] {} {} - {}", errorId, req.getMethod(), path, ex.toString(), ex);

    boolean ajax = isAjax(req);

    if (ajax) {
        String body = exposeDetails
                ? toJson(Map.of(
                    "errorId", errorId,
                    "status", status,
                    "message", userMessage,
                    "detail", ex.getMessage()))
                : toJson(Map.of(
                    "errorId", errorId,
                    "status", status,
                    "message", userMessage));

        return ResponseEntity
                .status(status)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body);
            } else {
                String msg = userMessage + " (Mã lỗi: " + errorId + ")";
                if (exposeDetails && ex.getMessage() != null && !ex.getMessage().isBlank()) {
                    msg += " - " + ex.getMessage();
                }

                // Thêm thông báo lỗi vào model
                ra.addFlashAttribute("error", msg); // Sử dụng Flash Attribute để hiển thị trên JSP

                // Trả về trang lỗi
                return "redirect:/errorPage";  // Đảm bảo bạn có trang JSP errorPage.jsp để hiển thị thông báo
            }
}


    private boolean isAjax(HttpServletRequest req) {
        String xr = req.getHeader("X-Requested-With");
        if (xr != null && "XMLHttpRequest".equalsIgnoreCase(xr)) return true;
        String accept = req.getHeader("Accept");
        return accept != null && accept.contains("application/json");
    }

    @SuppressWarnings("null")
    private String messageOf(FieldError fe) {
        return (fe.getDefaultMessage() != null && !fe.getDefaultMessage().isBlank())
                ? fe.getDefaultMessage()
                : "không hợp lệ";
    }

    private String toJson(Map<String, ?> map) {
        // JSON đơn giản (tránh thêm lib)
        return map.entrySet().stream()
                .map(e -> "\"" + escape(e.getKey()) + "\":\"" + escape(String.valueOf(e.getValue())) + "\"")
                .collect(Collectors.joining(",", "{", "}"));
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\")
                                 .replace("\"", "\\\"")
                                 .replace("\n", "\\n");
    }

    /** Trả về message an toàn cho người dùng; dev có thể bật chi tiết qua cấu hình. */
    private String safeMessage(String fallback, Exception ex) {
        String msg = (ex != null ? ex.getMessage() : null);
        if (exposeDetails && msg != null && !msg.isBlank()) return msg;
        return fallback;
    }
    
}