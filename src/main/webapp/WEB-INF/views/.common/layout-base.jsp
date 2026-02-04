<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.pageTitle} | Quản Lý Khách Sạn</title>

    <%-- Liên kết đến các file CSS --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/static/css/main-style.css' />">
    <link rel="stylesheet" href="<c:url value='/static/css/data-style.css' />">
    <link rel="stylesheet" href="<c:url value='/static/css/form-style.css' />">
    <link rel="stylesheet" href="<c:url value='/static/css/grid-style.css' />">
    <link rel="stylesheet" href="<c:url value='/font-6-pro/css/all.css' />">
</head>
<body>
<div class="app-container">
    <header class="app-header">
        <div class="header-left">
            <h3>HỆ THỐNG QUẢN LÝ</h3>
        </div>
        <div class="header-right">
            <span><sec:authentication property="principal.username" /></span>
        </div>
    </header>

    <nav class="app-sidebar">
        <ul>
            <li><a href="<c:url value='/' />">TRANG CHỦ</a></li>
            <hr>
            <li><strong>Quản Lý Phòng</strong></li>
            <li><a href="<c:url value='/phong' />">Phòng</a></li>
            <li><a href="<c:url value='/loaiphong' />">Loại Phòng</a></li>
            <li><a href="<c:url value='/thietbi' />">Thiết Bị</a></li>
            <li><a href="<c:url value='/thietbiphong' />">Thiết Bị trong Phòng</a></li>
            <hr>
            <li><strong>Nghiệp Vụ</strong></li>
            <li><a href="<c:url value='/datphong' />">Đặt Phòng</a></li>
            <li><a href="<c:url value='/dichvu' />">Dịch Vụ</a></li>
            <li><a href="<c:url value='/sddv' />">Sử Dụng Dịch Vụ</a></li>
            <li><a href="<c:url value='/chitietdichvu' />">Chi Tiết Dịch Vụ</a></li>
            <li><a href="<c:url value='/kiemtraphong' />">Kiểm Tra Phòng</a></li>
            <li><a href="<c:url value='/kiemtrachitiet' />">Chi Tiết Kiểm Tra</a></li>
            <hr>
            <li><strong>Thanh Toán</strong></li>
            <li><a href="<c:url value='/hoadon' />">Hóa Đơn</a></li>
            <li><a href="<c:url value='/hdct' />">Hóa Đơn Chi Tiết</a></li>
            <hr>
            <sec:authorize access="hasRole('ADMIN')">
                <li><strong>Quản Lý Chung</strong></li>
                <li><a href="<c:url value='/nhanvien' />">Nhân Viên</a></li>
                <li><a href="<c:url value='/khachhang' />">Khách Hàng</a></li>
                <li><a href="<c:url value='/users' />">Tài Khoản</a></li>
                <li><a href="<c:url value='/roles' />">Phân Quyền</a></li>
            </sec:authorize>
            <li><a href="<c:url value='/logout' />">Đăng Xuất</a></li>
        </ul>
    </nav>

    <main class="app-main-content">
        <jsp:include page="${param.mainContent}" />
    </main>

    <footer class="app-footer">
        <p>&copy; 2025 Quản Lý Khách Sạn. All rights reserved.</p>
    </footer>
</div>

<!-- Overlay chứa form được load bằng AJAX -->
<div id="formOverlay">
    <div id="formContainer">
        <span class="close-btn" onclick="closeForm()"><i class="fa-solid fa-xmark"></i></span>
        <div id="formContent">Đang tải...</div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="<c:url value='/static/js/main-script.js' />"></script>
<script src="<c:url value='/static/js/table.js' />"></script>
<script src="<c:url value='/static/js/grid.js' />"></script>
<script src="<c:url value='/static/js/form.js' />"></script>
<script>
    function loadForm(url) {
        $("#formContent").html("Đang tải...");
        $("#formContent").load(url, function(response, status, xhr) {
            if (status === "success") {
                $("#formOverlay").css("display", "flex");
                setTimeout(() => {
                    $("#formOverlay").addClass("active");
                }, 10);
            } else {
                $("#formContent").html("Lỗi tải dữ liệu!");
            }
        });
    }

    function closeForm() {
        $("#formOverlay").removeClass("active");
        setTimeout(() => {
            $("#formOverlay").css("display", "none");
            $("#formContent").html("");
        }, 300);
    }
</script>

<c:if test="${not empty param.pageScript}">
    <jsp:include page="${param.pageScript}" />
</c:if>
</body>
</html>