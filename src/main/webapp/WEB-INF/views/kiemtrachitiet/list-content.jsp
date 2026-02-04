<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Kiểm Tra Chi Tiết</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/kiemtrachitiet">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${param.keyword}"/>
            <select name="tinhTrang">
                <option value="">Tất cả tình trạng</option>
                <option value="TOT" ${param.tinhTrang == 'TOT' ? 'selected' : ''}>Tốt</option>
                <option value="HONG" ${param.tinhTrang == 'HONG' ? 'selected' : ''}>Hỏng</option>
            </select>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/kiemtrachitiet" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/kiemtrachitiet/add')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="item" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã KT">${item.id.maKiemTra}</div>
                <div class="data-cell" data-label="Mã TB">${item.id.maThietBi}</div>
                <div class="data-cell" data-label="Tình trạng">${item.tinhTrang}</div>
                <div class="data-cell" data-label="Số lượng hỏng">${item.soLuongHong}</div>
                <div class="data-cell" data-label="Đền bù">${item.denBu}</div>
                <div class="data-cell" data-label="Ngày KT">${item.kiemTraPhong.ngayKiemTraString}</div>
                <div class="data-cell" data-label="Ghi chú">${item.ghiChu}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/kiemtrachitiet/edit?maKiemTra=${item.id.maKiemTra}&maThietBi=${item.id.maThietBi}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/kiemtrachitiet/delete?maKiemTra=${item.id.maKiemTra}&maThietBi=${item.id.maThietBi}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="item" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã KT">${item.id.maKiemTra}</div>
                <div class="data-cell" data-label="Mã TB">${item.id.maThietBi}</div>
                <div class="data-cell" data-label="Tình trạng">${item.tinhTrang}</div>
                <div class="data-cell" data-label="Số lượng hỏng">${item.soLuongHong}</div>
                <div class="data-cell" data-label="Đền bù">${item.denBu}</div>
                <div class="data-cell" data-label="Ngày KT">${item.kiemTraPhong.ngayKiemTraString}</div>
                <div class="data-cell" data-label="Ghi chú">${item.ghiChu}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/kiemtrachitiet/edit?maKiemTra=${item.id.maKiemTra}&maThietBi=${item.id.maThietBi}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/kiemtrachitiet/delete?maKiemTra=${item.id.maKiemTra}&maThietBi=${item.id.maThietBi}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>