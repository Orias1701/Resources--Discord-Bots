<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Kiểm Tra Phòng</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/kiemtraphong">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${param.keyword}"/>
            <select name="trangThai">
                <option value="">Tất cả trạng thái</option>
                <c:forEach var="tt" items="${trangThaiOptions}">
                    <option value="${tt}">${tt}</option>
                </c:forEach>
            </select>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/kiemtraphong" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/kiemtraphong/form')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="k" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã KT">${k.maKiemTra}</div>
                <div class="data-cell" data-label="Phòng">${k.phong.tenPhong}</div>
                <div class="data-cell" data-label="Đặt Phòng">${k.datPhong.maDatPhong}</div>
                <div class="data-cell" data-label="Mã NV">${k.maNhanVien.maNhanVien}</div>
                <div class="data-cell" data-label="Ngày KT">${k.ngayKiemTra}</div>
                <div class="data-cell" data-label="Trạng thái">${k.trangThai}</div>
                <div class="data-cell" data-label="Tiền KT">${k.tienKiemTra}</div>
                <div class="data-cell" data-label="Tiền KT">${k.thanhToan}</div>
                <div class="data-cell" data-label="Ghi chú">${k.ghiChu}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/kiemtraphong/form?maKiemTra=${k.maKiemTra}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/kiemtraphong/delete?maKiemTra=${k.maKiemTra}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="k" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã KT">${k.maKiemTra}</div>
                <div class="data-cell" data-label="Phòng">${k.phong.tenPhong}</div>
                <div class="data-cell" data-label="Mã NV">${k.maNhanVien.maNhanVien}</div>
                <div class="data-cell" data-label="Ngày KT">${k.ngayKiemTra}</div>
                <div class="data-cell" data-label="Trạng thái">${k.trangThai}</div>
                <div class="data-cell" data-label="Tiền KT">${k.tienKiemTra}</div>
                <div class="data-cell" data-label="Tiền KT">${k.thanhToan}</div>
                <div class="data-cell" data-label="Ghi chú">${k.ghiChu}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/kiemtraphong/form?maKiemTra=${k.maKiemTra}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/kiemtraphong/delete?maKiemTra=${k.maKiemTra}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>