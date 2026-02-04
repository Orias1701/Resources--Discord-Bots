<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Sử Dụng Dịch Vụ</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/sddv">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${keyword}"/>
            <input type="date" name="startDate" value="${startDate}"/>
            <input type="date" name="endDate" value="${endDate}"/>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/sddv" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/sddv/form')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="sddv" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã SDDV">${sddv.maSDDV}</div>
                <div class="data-cell" data-label="Khách Hàng">${sddv.khachHang.tenKhachHang}</div>
                <div class="data-cell" data-label="Mã NV">${sddv.maNhanVien.maNhanVien}</div>
                <div class="data-cell" data-label="Ngày SDDV">${sddv.ngaySDDV}</div>
                <div class="data-cell" data-label="Tổng tiền">${sddv.tongTien}</div>
                <div class="data-cell" data-label="Tổng tiền">${sddv.thanhToan}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/sddv/form?maSDDV=${sddv.maSDDV}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/sddv/delete?maSDDV=${sddv.maSDDV}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="sddv" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã SDDV">${sddv.maSDDV}</div>
                <div class="data-cell" data-label="Khách Hàng">${sddv.khachHang.tenKhachHang}</div>
                <div class="data-cell" data-label="Mã NV">${sddv.maNhanVien.maNhanVien}</div>
                <div class="data-cell" data-label="Ngày SDDV">${sddv.ngaySDDV}</div>
                <div class="data-cell" data-label="Tổng tiền">${sddv.tongTien}</div>
                <div class="data-cell" data-label="Tổng tiền">${sddv.thanhToan}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/sddv/form?maSDDV=${sddv.maSDDV}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/sddv/delete?maSDDV=${sddv.maSDDV}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>