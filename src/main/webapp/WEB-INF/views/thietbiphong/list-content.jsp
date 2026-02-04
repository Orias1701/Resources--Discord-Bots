<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Thiết Bị Phòng</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/thietbiphong">
            <input type="text" name="q" value="${q}" placeholder="Mã phòng / Mã thiết bị"/>
            <select name="trangThai">
                <option value="">-- Tất cả trạng thái --</option>
                <c:forEach var="st" items="${allTrangThais}">
                    <option value="${st}" <c:if test="${trangThai eq st}">selected</c:if>>${st}</option>
                </c:forEach>
            </select>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/thietbiphong" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
        <a href="javascript:void(0)" onclick="loadForm('/thietbiphong/new')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="it" items="${items}">
            <div class="data-item">
                <div class="data-cell" data-label="Phòng">${it.phong.maPhong}</div>
                <div class="data-cell" data-label="Thiết bị">${it.thietBi.maThietBi}</div>
                <div class="data-cell" data-label="Số lượng">${it.soLuong}</div>
                <div class="data-cell" data-label="Trạng thái">${it.trangThai}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/thietbiphong/edit?maPhong=${it.phong.maPhong}&maThietBi=${it.thietBi.maThietBi}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/thietbiphong/delete?maPhong=${it.phong.maPhong}&maThietBi=${it.thietBi.maThietBi}" 
                       onclick="return confirm('Xoá TB của phòng ${it.phong.maPhong}?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="it" items="${items}">
            <div class="data-item">
                <div class="data-cell" data-label="Phòng">${it.phong.maPhong}</div>
                <div class="data-cell" data-label="Thiết bị">${it.thietBi.maThietBi}</div>
                <div class="data-cell" data-label="Số lượng">${it.soLuong}</div>
                <div class="data-cell" data-label="Trạng thái">${it.trangThai}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/thietbiphong/edit?maPhong=${it.phong.maPhong}&maThietBi=${it.thietBi.maThietBi}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/thietbiphong/delete?maPhong=${it.phong.maPhong}&maThietBi=${it.thietBi.maThietBi}" 
                       onclick="return confirm('Xoá TB của phòng ${it.phong.maPhong}?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>