<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<html>
<head>
    <title>Đặt phòng - Form</title>
    <style>
        body{font-family:system-ui,Arial;padding:16px}
        form{max-width:720px;margin:auto;display:grid;gap:12px}
        label{font-weight:600}
        input,select,button{padding:8px}
        .row{display:grid;grid-template-columns:1fr 1fr;gap:12px}
        .actions{display:flex;gap:8px;justify-content:flex-end;margin-top:8px}
        .hint{color:#666;font-size:12px}
    </style>
</head>
<body>

<h2>${datPhong.maDatPhong == null ? "Tạo đặt phòng" : "Sửa đặt phòng " += datPhong.maDatPhong}</h2>

<form method="post" action="${pageContext.request.contextPath}/datphong/save">
    <!-- Hidden ID nếu có -->
    <input type="hidden" name="maDatPhong" value="${datPhong.maDatPhong}"/>

    <!-- Khách hàng -->
    <label>Khách hàng</label>
    <select name="maKhachHang.maKhachHang" required>
        <c:forEach items="${khachHangList}" var="k">
            <option value="${k.maKhachHang}"
                <c:if test="${datPhong.maKhachHang != null && datPhong.maKhachHang.maKhachHang == k.maKhachHang}">selected</c:if>>
                ${k.maKhachHang}
            </option>
        </c:forEach>
    </select>

    <!-- Phòng -->
    <label>Phòng</label>
    <select name="maPhong.maPhong" required>
        <c:forEach items="${phongList}" var="p">
            <option value="${p.maPhong}"
                <c:if test="${datPhong.maPhong != null && datPhong.maPhong.maPhong == p.maPhong}">selected</c:if>>
                ${p.maPhong}
            </option>
        </c:forEach>
    </select>

    <!-- Nhân viên (có thể bỏ trống) -->
    <label>Nhân viên (tuỳ chọn)</label>
    <select name="maNhanVien.maNhanVien">
        <option value="">-- Không chọn --</option>
        <c:forEach items="${nhanVienList}" var="n">
            <option value="${n.maNhanVien}"
                <c:if test="${datPhong.maNhanVien != null && datPhong.maNhanVien.maNhanVien == n.maNhanVien}">selected</c:if>>
                ${n.maNhanVien}
            </option>
        </c:forEach>
    </select>

    <div class="row">
        <div>
            <label>Giờ nhận (ngayNhanPhong)</label>
            <input type="datetime-local" name="ngayNhanPhong"
                   value="${datPhong.ngayNhanPhong != null ? datPhong.ngayNhanPhong.toString().substring(0,16) : ''}" required/>
        </div>
        <div>
            <label>Giờ hẹn trả (ngayHenTra)</label>
            <input type="datetime-local" name="ngayHenTra"
                   value="${datPhong.ngayHenTra != null ? datPhong.ngayHenTra.toString().substring(0,16) : ''}" required/>
        </div>
    </div>

    <div class="row">
        <div>
            <label>Giờ trả thực tế (ngayTraPhong)</label>
            <input type="datetime-local" name="ngayTraPhong"
                   value="${datPhong.ngayTraPhong != null ? datPhong.ngayTraPhong.toString().substring(0,16) : ''}"/>
            <div class="hint">Có thể để trống khi khách chưa trả</div>
        </div>
        <div></div>
    </div>


<label>Cách đặt</label>
<select name="cachDat">
    <c:forEach items="${cachDatEnums}" var="cd">
        <option value="${cd}" <c:if test="${datPhong.cachDat == cd}">selected</c:if>>${cd}</option>
    </c:forEach>
</select>

<!-- Tình trạng -->
<label>Tình trạng</label>
<select name="tinhTrang">
    <c:forEach items="${tinhTrangEnums}" var="tt">
        <option value="${tt}" <c:if test="${datPhong.tinhTrang == tt}">selected</c:if>>${tt}</option>
    </c:forEach>
</select>


    <div class="actions">
        <a href="${pageContext.request.contextPath}/datphong">
            <button type="button">Quay lại</button>
        </a>
        <button type="submit">Lưu</button>
    </div>
</form>

</body>
</html>
