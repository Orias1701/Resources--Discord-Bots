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
    <title>Danh sach khach hang</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h2 class="mt-4">Danh sach khach hang</h2>
    <a href="/khachhang/add" class="btn btn-primary mb-3">Them khach hang</a>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Ma khach hang</th>
            <th>Ten khach hang</th>
            <th>Gioi tinh</th>
            <th>So dien thoai</th>
            <th>Tinh trang</th>
            <th>Hanh dong</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="khachHang" items="${khachHangList}">
            <tr>
                <td>${khachHang.maKhachHang}</td>
                <td>${khachHang.tenKhachHang}</td>
                <td>${khachHang.gioiTinh}</td>
                <td>${khachHang.sdt}</td>
                <td>${khachHang.tinhTrangKhach}</td>
                <td>
                    <a href="/khachhang/edit/${khachHang.maKhachHang}" class="btn btn-warning btn-sm">Sua</a>
                    <a href="/khachhang/delete/${khachHang.maKhachHang}" class="btn btn-danger btn-sm" onclick="return confirm('Ban co chac muon xoa?')">Xoa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>