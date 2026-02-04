<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách Chi Tiết Kiểm Tra</title>
    <style>
        .filter-container form {
            display: inline-block;
            margin-right: 20px;
        }
    </style>
</head>
<body>
<h2>Danh sách Chi Tiết Kiểm Tra</h2>

<div class="filter-container">
    <form method="get" action="/kiemtrachitiet">
        Tìm kiếm: <input type="text" name="keyword" placeholder="Nhập từ khóa..." value="${param.keyword}"/>
        <button type="submit">Tìm</button>
    </form>

    <form method="get" action="/kiemtrachitiet">
        Lọc theo Tình trạng:
        <select name="tinhTrang">
            <option value="">Tất cả</option>
            <option value="TOT" ${param.tinhTrang == 'TOT' ? 'selected' : ''}>Tốt</option>
            <option value="HONG" ${param.tinhTrang == 'HONG' ? 'selected' : ''}>Hỏng</option>
        </select>
        <button type="submit">Lọc</button>
    </form>
</div>

<a href="/kiemtrachitiet/add">Thêm mới Chi Tiết Kiểm Tra</a>

<table border="1" cellpadding="5">
    <tr>
        <th>Mã Kiểm Tra</th>
        <th>Mã Thiết Bị</th>
        <th>Tình Trạng</th>
        <th>Số lượng hỏng</th>
        <th>Đền Bù</th>
        <th>Ghi Chú</th>
        <th>Ngày Kiểm Tra</th>
        <th>Hành động</th>
    </tr>
    <c:forEach var="item" items="${list}">
        <tr>
            <td>${item.id.maKiemTra}</td>
            <td>${item.id.maThietBi}</td>
            <td>${item.tinhTrang}</td>
            <td>${item.soLuongHong}</td>
            <td><fmt:formatNumber value="${item.denBu}"  /></td>
            <td>${item.ghiChu}</td>
            <td>${item.kiemTraPhong.ngayKiemTraString}</td>
            <td>
                <a href="/kiemtrachitiet/edit?maKiemTra=${item.id.maKiemTra}&maThietBi=${item.id.maThietBi}">Sửa</a>
                <a href="/kiemtrachitiet/delete?maKiemTra=${item.id.maKiemTra}&maThietBi=${item.id.maThietBi}" onclick="return confirm('Bạn có muốn xóa?');">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>