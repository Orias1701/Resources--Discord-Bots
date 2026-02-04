<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>


<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách Kiểm Tra Phòng</title>
</head>
<body>
<h2>Danh sách Kiểm Tra Phòng</h2>

<form method="get" action="/kiemtraphong">
    Tìm kiếm: <input type="text" name="keyword" />
    <button type="submit">Tìm</button>
</form>

<form method="get" action="/kiemtraphong">
    Lọc theo trạng thái:
    <select name="trangThai">
        <option value="">Tất cả</option>
        <c:forEach var="tt" items="${trangThaiOptions}">
            <option value="${tt}">${tt}</option>
        </c:forEach>
    </select>
    <button type="submit">Lọc</button>
</form>

<a href="/kiemtraphong/form">Thêm mới Kiểm Tra</a>

<table border="1" cellpadding="5">
    <tr>
        <th>Mã Kiểm Tra</th>
        <th>Phòng</th>
        <th>Mã Nhân Viên</th>
        <th>Ngày Kiểm Tra</th>
        <th>Ghi Chú</th>
        <th>Trạng Thái</th>
        <th>Thanh Toán</th>
        <th>Tiền Kiểm Tra</th> <%-- THÊM CỘT MỚI --%>
        <th>Hành động</th>
    </tr>
    <c:forEach var="k" items="${list}">
        <tr>
            <td>${k.maKiemTra}</td>
            <td>${k.phong.tenPhong}</td>
            <td>${k.maNhanVien.maNhanVien}</td>
            <td>${k.ngayKiemTra}</td>
            <td>${k.ghiChu}</td>
            <td>${k.trangThai}</td>
            <td>${k.thanhToan}</td>
            <td>${k.tienKiemTra}</td> <%-- HIỂN THỊ TIỀN KIỂM TRA --%>
            <td>
                <a href="/kiemtraphong/form?maKiemTra=${k.maKiemTra}">Sửa</a>
                <a href="/kiemtraphong/delete?maKiemTra=${k.maKiemTra}" onclick="return confirm('Bạn có muốn xóa?');">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>