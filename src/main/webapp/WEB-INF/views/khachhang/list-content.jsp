<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Khách Hàng</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/khachhang">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${param.keyword}"/>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/khachhang" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/khachhang/add')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="kh" items="${khachHangList}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã KH">${kh.maKhachHang}</div>
                <div class="data-cell" data-label="Tên KH">${kh.tenKhachHang}</div>
                <div class="data-cell" data-label="Giới tính">${kh.gioiTinh}</div>
                <div class="data-cell" data-label="SĐT">${kh.sdt}</div>
                <div class="data-cell" data-label="Tình trạng">${kh.tinhTrangKhach}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/khachhang/edit/${kh.maKhachHang}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/khachhang/delete/${kh.maKhachHang}" onclick="return confirm('Bạn có chắc muốn xóa?')"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="kh" items="${khachHangList}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã KH">${kh.maKhachHang}</div>
                <div class="data-cell" data-label="Tên KH">${kh.tenKhachHang}</div>
                <div class="data-cell" data-label="Giới tính">${kh.gioiTinh}</div>
                <div class="data-cell" data-label="SĐT">${kh.sdt}</div>
                <div class="data-cell" data-label="Tình trạng">${kh.tinhTrangKhach}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/khachhang/edit/${kh.maKhachHang}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/khachhang/delete/${kh.maKhachHang}" onclick="return confirm('Bạn có chắc muốn xóa?')"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>