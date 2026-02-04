<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
  <title>Thiết bị phòng - Danh sách</title>
</head>
<body>
<h2>Thiết bị phòng</h2>

<c:if test="${not empty warn}">
  <p style="color:#b58900;"><b>${warn}</b></p>
</c:if>
<c:if test="${not empty success}">
  <p style="color:green;"><b>${success}</b></p>
</c:if>
<c:if test="${not empty error}">
  <p style="color:red;"><b>${error}</b></p>
</c:if>

<form method="get" action="${pageContext.request.contextPath}/thietbiphong">
  Từ khóa:
  <input type="text" name="q" value="${q}" placeholder="Mã phòng / Mã thiết bị"/>

  Trạng thái:
  <select name="trangThai">
    <option value="">-- Tất cả --</option>
    <c:forEach var="st" items="${allTrangThais}">
      <option value="${st}" <c:if test="${trangThai eq st}">selected</c:if>>${st}</option>
    </c:forEach>
  </select>

  <button type="submit">Lọc</button>
  <a href="${pageContext.request.contextPath}/thietbiphong">Xoá bộ lọc</a>
</form>

<p><a href="${pageContext.request.contextPath}/thietbiphong/new">+ Thêm</a></p>

<table border="1" cellpadding="6" cellspacing="0">
  <thead>
    <tr>
      <th>Phòng</th>
      <th>Thiết bị</th>
      <th>Số lượng</th>
      <th>Trạng thái</th>
      <th>Hành động</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="it" items="${items}">
      <tr>
        <td>${it.phong != null ? it.phong.maPhong : ''}</td>
        <td>${it.thietBi != null ? it.thietBi.maThietBi : ''}</td>
        <td>${it.soLuong}</td>
        <td>${it.trangThai}</td>
        <td>
          <!-- Edit theo cặp để tương thích với service.getById(maPhong, maThietBi) -->
          <a href="${pageContext.request.contextPath}/thietbiphong/edit?maPhong=${it.phong.maPhong}&maThietBi=${it.thietBi.maThietBi}">Sửa</a>

          <form action="${pageContext.request.contextPath}/thietbiphong/delete" method="post" style="display:inline;">
            <input type="hidden" name="maPhong" value="${it.phong.maPhong}"/>
            <input type="hidden" name="maThietBi" value="${it.thietBi.maThietBi}"/>
            <button type="submit" onclick="return confirm('Xoá TB của phòng ${it.phong.maPhong} - TB ${it.thietBi.maThietBi}?');">Xoá</button>
          </form>
        </td>
      </tr>
    </c:forEach>

    <c:if test="${empty items}">
      <tr><td colspan="5">Không có dữ liệu.</td></tr>
    </c:if>
  </tbody>
</table>

</body>
</html>
