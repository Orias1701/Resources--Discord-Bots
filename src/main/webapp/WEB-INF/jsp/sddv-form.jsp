<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <meta charset="UTF-8">
    <title>Form Sử Dụng Dịch Vụ</title>
</head>
<body>
<h2>Form Sử Dụng Dịch Vụ</h2>

<c:set var="isPaid" value="${sddv != null && sddv.thanhToan == 'DA_THANH_TOAN'}"/>

<form method="post" action="/sddv/save">
    <!-- Mã SDDV -->
    <label>Mã SDDV:</label>
    <input type="text" name="maSDDV" value="${sddv.maSDDV}" readonly />
    <br/>

    <!-- Khách Hàng -->
    <label>Khách Hàng:</label>
    <select name="khachHang.maKhachHang" <c:if test="${isPaid}">disabled</c:if>>
        <option value="">— Chọn khách hàng —</option>
        <c:forEach var="kh" items="${khachHangList}">
            <option value="${kh.maKhachHang}"
                <c:if test="${sddv.khachHang != null && sddv.khachHang.maKhachHang == kh.maKhachHang}">
                    selected
                </c:if>>
                ${kh.tenKhachHang}
            </option>
        </c:forEach>
    </select>
    <br/>

    <!-- Mã Nhân Viên -->
    <label>Mã Nhân Viên:</label>
    <select name="maNhanVien.maNhanVien" <c:if test="${isPaid}">disabled</c:if>>
        <option value="">— Chọn nhân viên —</option>
        <c:forEach var="nv" items="${nhanVienList}">
            <option value="${nv.maNhanVien}"
                <c:if test="${sddv.maNhanVien != null && sddv.maNhanVien.maNhanVien == nv.maNhanVien}">
                    selected
                </c:if>>
                ${nv.maNhanVien}
            </option>
        </c:forEach>
    </select>
    <br/>

    <!-- Ngày Sử Dụng DV -->
    <label>Ngày Sử Dụng DV:</label>
    <input type="date" name="ngaySDDV" value="${sddv.ngaySDDV}" <c:if test="${isPaid}">disabled</c:if> />
    <br/>

    <!-- Thanh Toán (hiển thị tiếng Việt, không submit) -->
    <label>Thanh Toán:</label>
    <input type="text"
           value="${sddv.thanhToan == 'DA_THANH_TOAN' ? 'Đã thanh toán' : 'Chưa thanh toán'}"
           disabled />
    <!-- Gửi enum thật để server bind (mặc định CHUA_THANH_TOAN nếu null) -->
    <input type="hidden" name="thanhToan"
           value="${empty sddv.thanhToan ? 'CHUA_THANH_TOAN' : sddv.thanhToan}" />
    <br/>

    <!-- Tổng Tiền -->
    <label>Tổng Tiền:</label>
    <input type="text" name="tongTien" value="${sddv.tongTien}" readonly/>
    <br/>

    <c:choose>
        <c:when test="${isPaid}">
            <button type="button" disabled>Đã thanh toán (khóa)</button>
            <a href="/sddv">Quay lại</a>
        </c:when>
        <c:otherwise>
            <button type="submit">Lưu</button>
            <a href="/sddv">Hủy</a>
        </c:otherwise>
    </c:choose>
</form>

</body>
</html>
