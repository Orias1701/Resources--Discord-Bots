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
  <title>Form Hóa đơn chi tiết</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <style>
    .form-group{margin-bottom:10px}
    .form-group label{display:inline-block;width:150px}
    .form-group input,.form-group select{width:250px}
    fieldset{max-width:440px}
  </style>
</head>
<body>
<h2>Form Hóa đơn chi tiết</h2>

<c:if test="${not empty error}">
  <div style="color:red">${error}</div>
</c:if>

<form method="post" action="/hdct/save">
  <c:if test="${hdct.id != null}">
    <input type="hidden" name="id" value="${hdct.id}"/>
  </c:if>

  <div class="form-group">
    <label>Mã Hóa Đơn:</label>
    <select name="hoaDon.maHoaDon" required>
      <c:forEach var="h" items="${hoaDonList}">
        <option value="${h.maHoaDon}" <c:if test="${hdct.hoaDon != null && hdct.hoaDon.maHoaDon == h.maHoaDon}">selected</c:if>>
          ${h.maHoaDon}
        </option>
      </c:forEach>
    </select>
  </div>

  <div class="form-group">
    <label>Mã Đặt Phòng:</label>
    <select id="maDatPhong" name="datPhong.maDatPhong">
      <option value="">-- Không chọn --</option>
      <c:forEach var="dp" items="${datPhongList}">
        <option value="${dp.maDatPhong}" <c:if test="${hdct.datPhong != null && hdct.datPhong.maDatPhong == dp.maDatPhong}">selected</c:if>>
          ${dp.maDatPhong}
        </option>
      </c:forEach>
    </select>
  </div>

  <div class="form-group">
    <label>Mã SDDV:</label>
    <select id="maSDDV" name="suDungDichVu.maSDDV">
      <option value="">-- Không chọn --</option>
      <c:forEach var="s" items="${sddvList}">
        <option value="${s.maSDDV}" <c:if test="${hdct.suDungDichVu != null && hdct.suDungDichVu.maSDDV == s.maSDDV}">selected</c:if>>
          ${s.maSDDV}
        </option>
      </c:forEach>
    </select>
  </div>

  <div class="form-group">
    <label>Mã Kiểm Tra:</label>
    <select id="maKiemTra" name="kiemTraPhong.maKiemTra">
      <option value="">-- Không chọn --</option>
      <c:forEach var="k" items="${kiemTraList}">
        <option value="${k.maKiemTra}" <c:if test="${hdct.kiemTraPhong != null && hdct.kiemTraPhong.maKiemTra == k.maKiemTra}">selected</c:if>>
          ${k.maKiemTra}
        </option>
      </c:forEach>
    </select>
  </div>

  <div class="form-group">
    <label>Trạng thái thanh toán:</label>
    <!-- HDCT được phép đổi trạng thái -->
    <select name="thanhToan">
      <option value="">-- Không chọn --</option>
      <c:forEach var="tt" items="${thanhToanOptions}">
        <option value="${tt}" <c:if test="${hdct.thanhToan == tt}">selected</c:if>>${tt}</option>
      </c:forEach>
    </select>
  </div>

  <fieldset>
    <legend>Số tiền (tham khảo)</legend>
    <div class="form-group">
      <label>Tiền đặt phòng:</label>
      <span id="tienDatPhongSpan"><c:out value="${hdct.tienDatPhong != null ? hdct.tienDatPhong : '0'}"/></span>
    </div>
    <div class="form-group">
      <label>Tiền SDDV:</label>
      <span id="tienSddvSpan"><c:out value="${hdct.tienSddv != null ? hdct.tienSddv : '0'}"/></span>
    </div>
    <div class="form-group">
      <label>Tiền kiểm tra:</label>
      <span id="tienKiemTraSpan"><c:out value="${hdct.tienKiemTra != null ? hdct.tienKiemTra : '0'}"/></span>
    </div>
    <div class="form-group">
      <label>Thành tiền:</label>
      <span id="thanhTienSpan"><c:out value="${hdct.thanhTien != null ? hdct.thanhTien : '0'}"/></span>
    </div>
  </fieldset>

  <c:if test="${not empty _csrf}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  </c:if>

  <br/>
  <button type="submit">Lưu</button>
  <a href="/hdct">Hủy</a>
</form>

<script>
$(function () {
  // Cập nhật tiền kiểm tra khi chọn mã kiểm tra
  function updateTienKiemTra() {
    var maKiemTra = $('#maKiemTra').val();
    var $span = $('#tienKiemTraSpan');
    if (!maKiemTra) { $span.text('0'); return; }
    $.get('/hdct/api/get-tien-kiem-tra', { maKiemTra: maKiemTra })
      .done(function (res) { $span.text(res != null ? res : '0'); })
      .fail(function () { $span.text('0'); });
  }
  $('#maKiemTra').on('change', updateTienKiemTra);
  updateTienKiemTra();
});
</script>
</body>
</html>
