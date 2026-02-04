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
    <title>${isEdit ? 'Sua khach hang' : 'Them khach hang'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h2 class="mt-4">${isEdit ? 'Sua khach hang' : 'Them khach hang'}</h2>
    <form action="/khachhang/save" method="post">
        <c:if test="${isEdit}">
            <input type="hidden" name="maKhachHang" value="${khachHang.maKhachHang}">
        </c:if>
        <div class="mb-3">
            <label for="maKhachHang" class="form-label">Ma khach hang</label>
            <input type="text" class="form-control" id="maKhachHang" name="maKhachHang" value="${khachHang.maKhachHang}" ${isEdit ? 'disabled' : 'required'}>
        </div>
        <div class="mb-3">
            <label for="tenKhachHang" class="form-label">Ten khach hang</label>
            <input type="text" class="form-control" id="tenKhachHang" name="tenKhachHang" value="${khachHang.tenKhachHang}" required>
        </div>
        <div class="mb-3">
            <label for="gioiTinh" class="form-label">Gioi tinh</label>
            <select class="form-select" id="gioiTinh" name="gioiTinh" required>
                <c:forEach var="gioiTinh" items="${gioiTinhList}">
                    <option value="${gioiTinh}" ${khachHang.gioiTinh == gioiTinh ? 'selected' : ''}>${gioiTinh}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label for="sdt" class="form-label">So dien thoai</label>
            <input type="text" class="form-control" id="sdt" name="sdt" value="${khachHang.sdt}" required>
        </div>
        <div class="mb-3">
            <label for="tinhTrangKhach" class="form-label">Tinh trang</label>
            <select class="form-select" id="tinhTrangKhach" name="tinhTrangKhach" required>
                <c:forEach var="tinhTrang" items="${tinhTrangKhachList}">
                    <option value="${tinhTrang}" ${khachHang.tinhTrangKhach == tinhTrang ? 'selected' : ''}>${tinhTrang}</option>
                </c:forEach>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">${isEdit ? 'Luu' : 'Them'}</button>
        <a href="/khachhang" class="btn btn-secondary">Huy</a>
    </form>
</div>
</body>
</html>