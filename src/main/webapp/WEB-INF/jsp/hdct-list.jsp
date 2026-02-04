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
  <title>Danh sách Hóa đơn chi tiết</title>
  <style>
    table{border-collapse:collapse} th,td{border:1px solid #ccc;padding:6px}
    .msg{margin:8px 0}
    form.inline{display:inline}
  </style>
</head>
<body>
<h2>Danh sách Hóa đơn chi tiết</h2>

<c:if test="${not empty message}"><div class="msg" style="color:green">${message}</div></c:if>
<c:if test="${not empty error}"><div class="msg" style="color:red">${error}</div></c:if>

<form method="get" action="/hdct" style="margin-bottom:10px;">
  Mã Hóa Đơn:
  <input type="text" name="maHoaDon" value="${maHoaDon}"/>
  <button type="submit">Lọc</button>
  <a href="/hdct" style="margin-left:8px;">Xóa lọc</a>
</form>

<p><a href="/hdct/form">Thêm Hóa đơn chi tiết</a></p>

<table>
  <thead>
  <tr>
    <th>ID</th>
    <th>Mã HĐ</th>
    <th>Mã Đặt Phòng</th>
    <th>Mã SDDV</th>
    <th>Mã Kiểm Tra</th>
    <th>Tiền đặt phòng</th>
    <th>Tiền SDDV</th>
    <th>Tiền kiểm tra</th>
    <th>Thành tiền</th>
    <th>Trạng thái</th>
    <th>Hành động</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="h" items="${list}">
    <tr>
      <td>${h.id}</td>
      <td><c:out value="${h.hoaDon != null ? h.hoaDon.maHoaDon : ''}"/></td>
      <td><c:out value="${h.datPhong != null ? h.datPhong.maDatPhong : ''}"/></td>
      <td><c:out value="${h.suDungDichVu != null ? h.suDungDichVu.maSDDV : ''}"/></td>
      <td><c:out value="${h.kiemTraPhong != null ? h.kiemTraPhong.maKiemTra : ''}"/></td>
      <td><fmt:formatNumber value="${h.tienDatPhong != null ? h.tienDatPhong : 0}" type="number" minFractionDigits="0"/></td>
      <td><fmt:formatNumber value="${h.tienSddv != null ? h.tienSddv : 0}" type="number" minFractionDigits="0"/></td>
      <td><fmt:formatNumber value="${h.tienKiemTra != null ? h.tienKiemTra : 0}" type="number" minFractionDigits="0"/></td>
      <td><fmt:formatNumber value="${h.thanhTien != null ? h.thanhTien : 0}" type="number" minFractionDigits="0"/></td>
      <td><c:out value="${h.thanhToan}"/></td>
      <td>
        <a href="/hdct/form?id=${h.id}">Sửa</a>
        &nbsp;|&nbsp;
        <a href="/hdct/delete?id=${h.id}" onclick="return confirm('Xóa HDCT #${h.id}?')">Xóa</a>
        &nbsp;|&nbsp;
        <!-- Mark PAID -->
        <form class="inline" method="post" action="/hdct/mark-paid">
          <input type="hidden" name="id" value="${h.id}"/>
          <c:if test="${not empty _csrf}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          </c:if>
          <button type="submit">Đã thanh toán</button>
        </form>
        &nbsp;
        <!-- (tuỳ chọn) Mark UNPAID -->
        <form class="inline" method="post" action="/hdct/mark-unpaid">
          <input type="hidden" name="id" value="${h.id}"/>
          <c:if test="${not empty _csrf}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          </c:if>
          <button type="submit">Chưa thanh toán</button>
        </form>
      </td>
    </tr>
  </c:forEach>

  <c:if test="${empty list}">
    <tr><td colspan="11" style="text-align:center;">Không có dữ liệu</td></tr>
  </c:if>
  </tbody>
</table>
</body>
</html>
