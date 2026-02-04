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
    <meta charset="UTF-8">
    <title>Danh sách chi tiết sử dụng dịch vụ</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 12px; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background: #f5f5f5; }
        .filters { display:flex; gap:10px; margin-top:10px; }
        input[type="text"], input[type="number"] { padding:6px; }
        .actions a { margin-right:8px; }
    </style>
</head>
<body>

<h2>Chi tiết sử dụng dịch vụ</h2>

<form method="get" action="${pageContext.request.contextPath}/chitietdichvu" class="filters">
    <input type="text" name="keyword" placeholder="Tìm kiếm..."
           value="${keyword != null ? keyword : ''}" />
    <input type="number" name="minSL" placeholder="SL từ" value="${minSL}" />
    <input type="number" name="maxSL" placeholder="SL đến" value="${maxSL}" />
    <button type="submit">Lọc</button>
    <a href="${pageContext.request.contextPath}/chitietdichvu">Reset</a>
</form>

<div style="margin-top:12px;">
    <a href="${pageContext.request.contextPath}/chitietdichvu/form">+ Thêm mới</a>
</div>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>MaSDDV</th>
            <th>Mã DV</th>
            <th>Số lượng</th>
            <th>Thành tiền</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="ct" items="${list}">
            <tr>
                <td>${ct.id}</td>
                <td>${ct.suDungDichVu != null ? ct.suDungDichVu.maSDDV : ''}</td>
                <td>${ct.dichVu != null ? ct.dichVu.maDichVu : ''}</td>
                <td>${ct.soLuong}</td>
                <td>${ct.thanhTien}</td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/chitietdichvu/form?id=${ct.id}">Sửa</a>
                    <a href="${pageContext.request.contextPath}/chitietdichvu/delete?id=${ct.id}"
                       onclick="return confirm('Bạn có muốn xóa?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty list}">
            <tr><td colspan="6" style="text-align:center;">Không có dữ liệu</td></tr>
        </c:if>
    </tbody>
</table>

</body>
</html>
