<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <title>Quản lý Chi Tiết Kiểm Tra</title>
</head>
<body>
<h2>Quản lý Chi Tiết Kiểm Tra</h2>

<form action="/kiemtrachitiet/save" method="post">
    <input type="hidden" name="maKiemTra" value="${kiemTraChiTiet.id.maKiemTra}">
    <input type="hidden" name="maThietBi" value="${kiemTraChiTiet.id.maThietBi}">

    Mã Kiểm Tra: <input type="text" name="maKiemTra" value="${kiemTraChiTiet.id.maKiemTra}" <c:if test="${kiemTraChiTiet.id.maKiemTra != null}">readonly</c:if>/> <br/>
    Mã Thiết Bị: <input type="text" name="maThietBi" value="${kiemTraChiTiet.id.maThietBi}" <c:if test="${kiemTraChiTiet.id.maThietBi != null}">readonly</c:if>/> <br/>
    Tình Trạng: <br/>
    <label><input type="radio" name="tinhTrang" value="TOT" <c:if test="${kiemTraChiTiet.tinhTrang eq 'TOT' or kiemTraChiTiet.id == null}">checked</c:if>/> Tốt</label>
    <label><input type="radio" name="tinhTrang" value="HONG" <c:if test="${kiemTraChiTiet.tinhTrang eq 'HONG'}">checked</c:if>/> Hỏng</label> <br/>
    Ghi chú: <input type="text" name="ghiChu" value="${kiemTraChiTiet.ghiChu}"/> <br/>

    <button type="submit">Lưu</button>
    <c:if test="${kiemTraChiTiet.id.maKiemTra != null}">
        <a href="/kiemtrachitiet">Hủy</a>
    </c:if>
</form>

<hr/>

<h3>Danh Sách Chi Tiết</h3>
<table border="1" cellpadding="5">
    <tr>
        <th>Mã Kiểm Tra</th>
        <th>Mã Thiết Bị</th>
        <th>Tình Trạng</th>
        <th>Đền Bù</th>
        <th>Ghi Chú</th>
        <th>Hành động</th>
    </tr>
    <c:forEach var="item" items="${list}">
        <tr>
            <td>${item.id.maKiemTra}</td>
            <td>${item.id.maThietBi}</td>
            <td>${item.tinhTrang}</td>
            <td>${item.denBu}</td>
            <td>${item.ghiChu}</td>
            <td>
                <a href="/kiemtrachitiet?maKiemTra=${item.id.maKiemTra}&maThietBi=${item.id.maThietBi}">Sửa</a>
                <a href="/kiemtrachitiet/delete?maKiemTra=${item.id.maKiemTra}&maThietBi=${item.id.maThietBi}" onclick="return confirm('Bạn có muốn xóa?');">Xóa</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>