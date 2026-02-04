<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Chi Tiết Dịch Vụ</h2>
    
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/chitietdichvu">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${keyword}"/>
            <input type="number" name="minSL" placeholder="SL từ" value="${minSL}"/>
            <input type="number" name="maxSL" placeholder="SL đến" value="${maxSL}"/>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/chitietdichvu" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/chitietdichvu/form')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="ct" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="ID">${ct.id}</div>
                <div class="data-cell" data-label="Mã SDDV">${ct.suDungDichVu.maSDDV}</div>
                <div class="data-cell" data-label="Mã DV">${ct.dichVu.maDichVu}</div>
                <div class="data-cell" data-label="Số lượng">${ct.soLuong}</div>
                <div class="data-cell" data-label="Thành tiền">${ct.thanhTien}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/chitietdichvu/form?id=${ct.id}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/chitietdichvu/delete?id=${ct.id}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="ct" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="ID">${ct.id}</div>
                <div class="data-cell" data-label="Mã SDDV">${ct.suDungDichVu.maSDDV}</div>
                <div class="data-cell" data-label="Mã DV">${ct.dichVu.maDichVu}</div>
                <div class="data-cell" data-label="Số lượng">${ct.soLuong}</div>
                <div class="data-cell" data-label="Thành tiền">${ct.thanhTien}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/chitietdichvu/form?id=${ct.id}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/chitietdichvu/delete?id=${ct.id}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>