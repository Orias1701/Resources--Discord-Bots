<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Dịch Vụ</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/dichvu">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${param.keyword}"/>
            <input type="text" name="minPrice" placeholder="Giá từ" value="${param.minPrice}"/>
            <input type="text" name="maxPrice" placeholder="Giá đến" value="${param.maxPrice}"/>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/dichvu" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/dichvu/form')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="dv" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã Dịch Vụ">${dv.maDichVu}</div>
                <div class="data-cell" data-label="Tên Dịch Vụ">${dv.tenDichVu}</div>
                <div class="data-cell" data-label="Loại Dịch Vụ">${dv.loaiDichVu}</div>
                <div class="data-cell" data-label="Giá">${dv.giaDichVu}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/dichvu/form?maDichVu=${dv.maDichVu}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/dichvu/delete?maDichVu=${dv.maDichVu}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="dv" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã Dịch Vụ">${dv.maDichVu}</div>
                <div class="data-cell" data-label="Tên Dịch Vụ">${dv.tenDichVu}</div>
                <div class="data-cell" data-label="Loại Dịch Vụ">${dv.loaiDichVu}</div>
                <div class="data-cell" data-label="Giá">${dv.giaDichVu}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/dichvu/form?maDichVu=${dv.maDichVu}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/dichvu/delete?maDichVu=${dv.maDichVu}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>