<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<html>
<head>
  <meta charset="UTF-8"/>
  <title>Danh sách Hóa đơn</title>
  <style>
    table{border-collapse:collapse} th,td{border:1px solid #ccc;padding:6px}
    .msg{margin:8px 0}
    form.inline{display:inline}
  </style>
</head>
<body>
<h2>Danh sách Hóa đơn</h2>

<c:if test="${not empty message}"><div class="msg" style="color:green">${message}</div></c:if>
<c:if test="${not empty error}"><div class="msg" style="color:red">${error}</div></c:if>

<form method="get" action="/hoadon" style="margin-bottom:10px;">
  Tìm kiếm:
  <input type="text" name="keyword" value="${keyword}"/>
  <button type="submit">Lọc</button>
  <a href="/hoadon" style="margin-left:8px;">Xóa lọc</a>
</form>

<p><a href="/hoadon/form">Thêm Hóa đơn</a></p>

<table>
  <thead>
  <tr>
    <th>Mã HĐ</th>
    <th>Nhân viên</th>
    <th>Khách hàng</th>
    <th>Ngày</th>
    <th>Tổng tiền</th>
    <th>Trạng thái</th>
    <th>Hành động</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="h" items="${list}">
    <tr>
      <td>${h.maHoaDon}</td>
      <td><c:out value="${h.maNhanVien != null ? h.maNhanVien.maNhanVien : ''}"/></td>
      <td><c:out value="${h.khachHang != null ? h.khachHang.maKhachHang : ''}"/></td>
      <td><c:out value="${h.ngay}"/></td>
      <td><fmt:formatNumber value="${h.tongTien}" type="number" minFractionDigits="0"/></td>
      <td><c:out value="${h.thanhToan}"/></td>
      <td>
        <a href="/hoadon/form?maHoaDon=${h.maHoaDon}">Sửa</a>
        &nbsp;|&nbsp;
        <a href="/hoadon/delete?maHoaDon=${h.maHoaDon}" onclick="return confirm('Xóa hóa đơn ${h.maHoaDon}?')">Xóa</a>
        &nbsp;|&nbsp;
        <!-- Nút đánh dấu ĐÃ THANH TOÁN -->
        <form class="inline" method="post" action="/hoadon/mark-paid">
          <input type="hidden" name="maHoaDon" value="${h.maHoaDon}"/>
          <c:if test="${not empty _csrf}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          </c:if>
          <button type="submit">Đánh dấu ĐÃ THANH TOÁN</button>
        </form>
      </td>
    </tr>
  </c:forEach>

  <c:if test="${empty list}">
    <tr><td colspan="7" style="text-align:center;">Không có dữ liệu</td></tr>
  </c:if>
  </tbody>
</table>
</body>
</html>
