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
    <title>Danh sách đặt phòng</title>
    <style>
        body{font-family:system-ui,Arial;padding:16px}
        .toolbar{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px}
        table{width:100%;border-collapse:collapse}
        th,td{border:1px solid #ddd;padding:8px}
        th{background:#fafafa;text-align:left}
        form.search{display:grid;grid-template-columns:repeat(6,1fr);gap:8px;margin:16px 0}
        .actions a, .actions form{display:inline-block;margin-right:6px}
        .flash{padding:8px;margin-bottom:12px;border-radius:6px}
        .success{background:#e9f9ee;border:1px solid #b6e1c6}
        .error{background:#fdecec;border:1px solid #f5b3b3}
        .btn{padding:6px 10px;display:inline-block;border:1px solid #ccc;border-radius:6px;text-decoration:none}
    </style>
</head>
<body>

<div class="toolbar">
    <h2>Đặt phòng</h2>
    <a class="btn" href="${pageContext.request.contextPath}/datphong/form">+ Tạo mới</a>
</div>

<c:if test="${not empty successMessage}">
    <div class="flash success">${successMessage}</div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="flash error">${errorMessage}</div>
</c:if>

<form class="search" method="get" action="${pageContext.request.contextPath}/datphong">
    <input type="text" name="keyword" placeholder="Từ khoá"
           value="${keyword}"/>

    <select name="maNhanVien">
        <option value="">-- Nhân viên --</option>
        <c:forEach items="${nhanVienList}" var="n">
            <option value="${n.maNhanVien}" <c:if test="${maNhanVien == n.maNhanVien}">selected</c:if>>
                ${n.maNhanVien}
            </option>
        </c:forEach>
    </select>

    <select name="maKhachHang">
        <option value="">-- Khách hàng --</option>
        <c:forEach items="${khachHangList}" var="k">
            <option value="${k.maKhachHang}" <c:if test="${maKhachHang == k.maKhachHang}">selected</c:if>>
                ${k.maKhachHang}
            </option>
        </c:forEach>
    </select>

    <select name="maPhong">
        <option value="">-- Phòng --</option>
        <c:forEach items="${phongList}" var="p">
            <option value="${p.maPhong}" <c:if test="${maPhong == p.maPhong}">selected</c:if>>
                ${p.maPhong}
            </option>
        </c:forEach>
    </select>

    <!-- SỬA Ở ĐÂY: dùng tinhTrangEnums đưa từ Controller -->
    <select name="tinhTrang">
        <option value="">-- Tình trạng --</option>
        <c:forEach items="${tinhTrangEnums}" var="tt">
            <option value="${tt}" <c:if test="${tinhTrang == tt.name()}">selected</c:if>>${tt}</option>
        </c:forEach>
    </select>

    <input type="datetime-local" name="fromDate"
           value="${fromDate != null ? fromDate.toString().substring(0,16) : ''}"/>
    <input type="datetime-local" name="toDate"
           value="${toDate != null ? toDate.toString().substring(0,16) : ''}"/>

    <button type="submit">Lọc</button>
</form>

<table>
    <thead>
    <tr>
        <th>Mã</th>
        <th>Khách</th>
        <th>Phòng</th>
        <th>Nhận</th>
        <th>Hẹn trả</th>
        <th>Trả</th>
        <th>Tình trạng</th>
        <th>Tiền phạt</th>
        <th>Thanh toán</th>
        <th>Tổng tiền</th>
        <th>Thao tác</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="dp">
        <tr>
            <td>${dp.maDatPhong}</td>
            <td>${dp.maKhachHang != null ? dp.maKhachHang.maKhachHang : ''}</td>
            <td>${dp.maPhong != null ? dp.maPhong.maPhong : ''}</td>
            <td>${dp.ngayNhanPhong}</td>
            <td>${dp.ngayHenTra}</td>
            <td>${dp.ngayTraPhong}</td>
            <td>${dp.tinhTrang}</td>
            <td>${dp.tienPhat}</td>
            <td>${dp.thanhToan}</td>
            <td>${dp.tongTien}</td>
            <td class="actions">
                <a class="btn" href="${pageContext.request.contextPath}/datphong/form?maDatPhong=${dp.maDatPhong}">Sửa</a>
                <a class="btn" href="${pageContext.request.contextPath}/datphong/delete?maDatPhong=${dp.maDatPhong}"
                   onclick="return confirm('Xoá đặt phòng ${dp.maDatPhong}?');">Xoá</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
