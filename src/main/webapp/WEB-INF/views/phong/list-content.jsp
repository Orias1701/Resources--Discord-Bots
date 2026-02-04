<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page-header">
    <h2>Danh sách Phòng</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/phong">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${param.keyword}"/>
            <select name="tinhTrang">
                <option value="">Tất cả tình trạng</option>
                <c:forEach var="tt" items="${tinhTrangOptions}">
                    <option value="${tt}" ${param.tinhTrang == tt ? 'selected' : ''}>${tt}</option>
                </c:forEach>
            </select>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/phong" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/phong/form')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="p" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã Phòng">${p.maPhong}</div>
                <div class="data-cell" data-label="Tên Phòng">${p.tenPhong}</div>
                <div class="data-cell" data-label="Loại Phòng">${p.maLoai.tenLoai}</div>
                <div class="data-cell" data-label="Mô tả">${p.moTa}</div>
                <div class="data-cell" data-label="Tình Trạng">${p.tinhTrangPhong}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/phong/form?maPhong=${p.maPhong}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/phong/delete?maPhong=${p.maPhong}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="p" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã Phòng">${p.maPhong}</div>
                <div class="data-cell" data-label="Tên Phòng">${p.tenPhong}</div>
                <div class="data-cell" data-label="Loại Phòng">${p.maLoai.tenLoai}</div>
                <div class="data-cell" data-label="Mô tả">${p.moTa}</div>
                <div class="data-cell" data-label="Tình Trạng">${p.tinhTrangPhong}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/phong/form?maPhong=${p.maPhong}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/phong/delete?maPhong=${p.maPhong}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>