<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
  <title>Form Hóa đơn</title>
  <style>
    .form-group{margin-bottom:10px}
    .form-group label{display:inline-block;width:150px}
    .form-group input,.form-group select{width:250px}
  </style>
</head>
<body>
<h2>Form Hóa đơn</h2>

<c:if test="${not empty error}">
  <div style="color:red">${error}</div>
</c:if>

<form method="post" action="/hoadon/save">
  <c:if test="${hd.maHoaDon != null}">
    <!-- Nếu sửa, có thể cho readonly để tránh đổi khóa -->
    <div class="form-group">
      <label>Mã Hóa Đơn:</label>
      <input type="text" name="maHoaDon" value="${hd.maHoaDon}" readonly/>
    </div>
  </c:if>
  <c:if test="${hd.maHoaDon == null}">
    <div class="form-group">
      <label>Mã Hóa Đơn:</label>
      <input type="text" name="maHoaDon" value="${hd.maHoaDon}"/>
    </div>
  </c:if>

  <div class="form-group">
    <label>Nhân viên:</label>
    <select name="maNhanVien.maNhanVien">
      <option value="">-- Không chọn --</option>
      <c:forEach var="nv" items="${nhanVienList}">
        <option value="${nv.maNhanVien}"
          <c:if test="${hd.maNhanVien != null && hd.maNhanVien.maNhanVien == nv.maNhanVien}">selected</c:if>>
          ${nv.maNhanVien}
        </option>
      </c:forEach>
    </select>
  </div>

  <div class="form-group">
    <label>Khách hàng:</label>
    <select name="khachHang.maKhachHang">
      <option value="">-- Không chọn --</option>
      <c:forEach var="kh" items="${khachHangList}">
        <option value="${kh.maKhachHang}"
          <c:if test="${hd.khachHang != null && hd.khachHang.maKhachHang == kh.maKhachHang}">selected</c:if>>
          ${kh.maKhachHang}
        </option>
      </c:forEach>
    </select>
  </div>

  <div class="form-group">
    <label>Ngày:</label>
    <input type="date" name="ngay" value="${hd.ngay}"/>
  </div>

  <div class="form-group">
    <label>Trạng thái thanh toán:</label>
    <!-- HD được phép đổi trạng thái -->
    <select name="thanhToan">
      <option value="">-- Không chọn --</option>
      <option value="CHUA_THANH_TOAN" <c:if test="${hd.thanhToan == 'CHUA_THANH_TOAN'}">selected</c:if>>CHUA_THANH_TOAN</option>
      <option value="DA_THANH_TOAN"   <c:if test="${hd.thanhToan == 'DA_THANH_TOAN'}">selected</c:if>>DA_THANH_TOAN</option>
    </select>
  </div>

  <div class="form-group">
    <label>Tổng tiền (tham khảo):</label>
    <input type="text" value="${hd.tongTien}" readonly/>
  </div>

  <c:if test="${not empty _csrf}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  </c:if>

  <button type="submit">Lưu</button>
  <a href="/hoadon">Hủy</a>
</form>

</body>
</html>
