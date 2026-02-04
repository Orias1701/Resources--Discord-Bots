<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Hóa Đơn Chi Tiết</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/hdct">
            <input type="text" name="maHoaDon" placeholder="Nhập mã hóa đơn..." value="${maHoaDon}"/>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/hdct" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/hdct/form')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="h" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="ID">${h.id}</div>
                <div class="data-cell" data-label="Mã HĐ">${h.hoaDon.maHoaDon}</div>
                <div class="data-cell" data-label="Mã Đặt Phòng">${h.datPhong.maDatPhong}</div>
                <div class="data-cell" data-label="Mã SDDV">${h.suDungDichVu.maSDDV}</div>
                <div class="data-cell" data-label="Mã Kiểm Tra">${h.kiemTraPhong.maKiemTra}</div>
                <div class="data-cell" data-label="Tiền đặt phòng">${h.tienDatPhong}</div>
                <div class="data-cell" data-label="Tiền SDDV">${h.tienSddv}</div>
                <div class="data-cell" data-label="Tiền kiểm tra">${h.tienKiemTra}</div>
                <div class="data-cell" data-label="Thành tiền">${h.thanhTien}</div>
                <div class="data-cell" data-label="Trạng thái">${h.thanhToan}</div>
                <div class="item-actions">
                    <c:if test="${h.thanhToan eq 'DA_THANH_TOAN'}">
                        <form class="inline" method="post" action="/hdct/mark-unpaid">
                            <input type="hidden" name="id" value="${h.id}"/>
                            <button type="submit"><i class="fa-solid fa-check"></i></button>
                        </form>
                    </c:if>

                    <c:if test="${h.thanhToan eq 'CHUA_THANH_TOAN'}">
                        <form class="inline" method="post" action="/hdct/mark-paid">
                            <input type="hidden" name="id" value="${h.id}"/>
                            <button type="submit"><i class="fa-solid fa-check"></i></button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="h" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="ID">${h.id}</div>
                <div class="data-cell" data-label="Mã HĐ">${h.hoaDon.maHoaDon}</div>
                <div class="data-cell" data-label="Mã Đặt Phòng">${h.datPhong.maDatPhong}</div>
                <div class="data-cell" data-label="Mã SDDV">${h.suDungDichVu.maSDDV}</div>
                <div class="data-cell" data-label="Mã Kiểm Tra">${h.kiemTraPhong.maKiemTra}</div>
                <div class="data-cell" data-label="Tiền đặt phòng">${h.tienDatPhong}</div>
                <div class="data-cell" data-label="Tiền SDDV">${h.tienSddv}</div>
                <div class="data-cell" data-label="Tiền kiểm tra">${h.tienKiemTra}</div>
                <div class="data-cell" data-label="Thành tiền">${h.thanhTien}</div>
                <div class="data-cell" data-label="Trạng thái">${h.thanhToan}</div>
                <div class="item-actions">
                    <c:if test="${h.thanhToan eq 'DA_THANH_TOAN'}">
                        <form class="inline" method="post" action="/hdct/mark-unpaid">
                            <input type="hidden" name="id" value="${h.id}"/>
                            <button type="submit"><i class="fa-solid fa-check"></i></button>
                        </form>
                    </c:if>

                    <c:if test="${h.thanhToan eq 'CHUA_THANH_TOAN'}">
                        <form class="inline" method="post" action="/hdct/mark-paid">
                            <input type="hidden" name="id" value="${h.id}"/>
                            <button type="submit"><i class="fa-solid fa-check"></i></button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
</div>