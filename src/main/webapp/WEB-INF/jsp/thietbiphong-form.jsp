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
  <title>${tbp != null && tbp.id != null ? "Sửa" : "Thêm"} Thiết bị phòng</title>
</head>
<body>
<h2>${tbp != null && tbp.id != null ? "Sửa" : "Thêm"} Thiết bị phòng</h2>

<c:if test="${not empty warn}">
  <p style="color:#b58900;"><b>${warn}</b></p>
</c:if>
<c:if test="${not empty success}">
  <p style="color:green;"><b>${success}</b></p>
</c:if>
<c:if test="${not empty error}">
  <p style="color:red;"><b>${error}</b></p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/thietbiphong/save">
  <!-- giữ cặp cũ khi edit để controller biết có đổi cặp không -->
  <input type="hidden" name="oldMaPhong" value="${oldMaPhong}"/>
  <input type="hidden" name="oldMaThietBi" value="${oldMaThietBi}"/>

  <div>
    <label>Phòng:</label>
    <select name="maPhong" required>
      <option value="">-- Chọn phòng --</option>
      <c:forEach var="p" items="${listPhong}">
        <option value="${p.maPhong}"
          <c:if test="${tbp != null && tbp.phong != null && tbp.phong.maPhong eq p.maPhong}">selected</c:if>>
          ${p.maPhong} - ${p.tenPhong}
        </option>
      </c:forEach>
    </select>
  </div>

  <div>
    <label>Thiết bị:</label>
    <select name="maThietBi" required>
      <option value="">-- Chọn thiết bị --</option>
      <c:forEach var="t" items="${listThietBi}">
        <option value="${t.maThietBi}"
          <c:if test="${tbp != null && tbp.thietBi != null && tbp.thietBi.maThietBi eq t.maThietBi}">selected</c:if>>
          ${t.maThietBi} - ${t.tenThietBi}
        </option>
      </c:forEach>
    </select>
  </div>

  <div>
    <label>Số lượng:</label>
    <input type="number" min="0" name="soLuong" value="${tbp.soLuong != null ? tbp.soLuong : 0}" required/>
  </div>

  <div>
    <label>Trạng thái:</label>
    <select name="trangThai" required>
      <c:forEach var="st" items="${allTrangThais}">
        <option value="${st}" <c:if test="${tbp.trangThai eq st}">selected</c:if>>${st}</option>
      </c:forEach>
    </select>
  </div>

  <div style="margin-top:10px;">
    <button type="submit">Lưu</button>
    <a href="${pageContext.request.contextPath}/thietbiphong">Hủy</a>
  </div>
</form>

</body>
</html>
