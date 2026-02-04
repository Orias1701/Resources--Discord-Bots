<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Đặt Phòng</h2>
</div>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
 </c:if>
 <div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>
<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/datphong">
            <input type="text" name="keyword" placeholder="Tìm từ khoá..." value="${keyword}"/>
            <select name="tinhTrang">
                <option value="">-- Tình trạng --</option>
                <c:forEach items="${tinhTrangEnums}" var="tt"><option value="${tt}" <c:if test="${tinhTrang == tt.name()}">selected</c:if>>${tt}</option></c:forEach>
            </select>
            <input type="datetime-local" name="fromDate" value="${fromDate != null ? fromDate.toString().substring(0,16) : ''}"/>
            <input type="datetime-local" name="toDate" value="${toDate != null ? toDate.toString().substring(0,16) : ''}"/>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/datphong" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/datphong/form')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach items="${list}" var="dp">
            <div class="data-item">
                <div class="data-cell" data-label="Mã ĐP">${dp.maDatPhong}</div>
                <div class="data-cell" data-label="Phòng">${dp.maPhong.maPhong}</div>
                <div class="data-cell" data-label="Nhân viên">${dp.maNhanVien.maNhanVien}</div>
                <div class="data-cell" data-label="Khách hàng">${dp.maKhachHang.maKhachHang}</div>
                <div class="data-cell" data-label="Ngày nhận">${dp.ngayNhanPhong}</div>
                <div class="data-cell" data-label="Ngày trả">${dp.ngayTraPhong}</div>
                <div class="data-cell" data-label="Ngày hẹn trả">${dp.ngayHenTra}</div>
                <div class="data-cell" data-label="Cách đặt">${dp.cachDat}</div>
                <div class="data-cell" data-label="Tình trạng">${dp.tinhTrang}</div>
                <div class="data-cell" data-label="Tiền phạt">${dp.tienPhat}</div>
                <div class="data-cell" data-label="Tổng tiền">${dp.tongTien}</div>
                <div class="data-cell" data-label="Thanh toán">${dp.thanhToan}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/datphong/form?maDatPhong=${dp.maDatPhong}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/datphong/delete?maDatPhong=${dp.maDatPhong}" onclick="return confirm('Xoá đặt phòng ${dp.maDatPhong}?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach items="${list}" var="dp">
            <div class="data-item">
                <div class="data-cell" data-label="Mã ĐP">${dp.maDatPhong}</div>
                <div class="data-cell" data-label="Phòng">${dp.maPhong.maPhong}</div>
                <div class="data-cell" data-label="Nhân viên">${dp.maNhanVien.maNhanVien}</div>
                <div class="data-cell" data-label="Khách hàng">${dp.maKhachHang.maKhachHang}</div>
                <div class="data-cell" data-label="Ngày nhận">${dp.ngayNhanPhong}</div>
                <div class="data-cell" data-label="Ngày trả">${dp.ngayTraPhong}</div>
                <div class="data-cell" data-label="Ngày hẹn trả">${dp.ngayHenTra}</div>
                <div class="data-cell" data-label="Cách đặt">${dp.cachDat}</div>
                <div class="data-cell" data-label="Tình trạng">${dp.tinhTrang}</div>
                <div class="data-cell" data-label="Tiền phạt">${dp.tienPhat}</div>
                <div class="data-cell" data-label="Tổng tiền">${dp.tongTien}</div>
                <div class="data-cell" data-label="Thanh toán">${dp.thanhToan}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/datphong/form?maDatPhong=${dp.maDatPhong}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/datphong/delete?maDatPhong=${dp.maDatPhong}" onclick="return confirm('Xoá đặt phòng ${dp.maDatPhong}?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>