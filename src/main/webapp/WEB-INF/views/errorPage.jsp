<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Error Page</title>
</head>
<body style="font-family: Arial; margin: 50px;">
    <h2 style="color: red;">Đã xảy ra lỗi!</h2>
    <p><strong>Mã lỗi:</strong> <%= request.getAttribute("javax.servlet.error.status_code") %></p>
    <p><strong>Thông báo:</strong> <%= request.getAttribute("javax.servlet.error.message") %></p>
    <a href="/">⬅ Quay lại trang chủ</a>
</body>
</html>
