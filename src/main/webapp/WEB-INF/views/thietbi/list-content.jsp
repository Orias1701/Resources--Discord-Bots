<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Thiết Bị</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/thietbi">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${keyword}"/>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/thietbi" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
         <a href="javascript:void(0)" onclick="loadForm('/thietbi/form')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="tb" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã TB">${tb.maThietBi}</div>
                <div class="data-cell" data-label="Tên TB">${tb.tenThietBi}</div>
                <div class="data-cell" data-label="Đơn Giá">${tb.donGia}</div>
                <div class="data-cell" data-label="Đền Bù">${tb.denBu}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/thietbi/form?maThietBi=${tb.maThietBi}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/thietbi/delete?maThietBi=${tb.maThietBi}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="tb" items="${list}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã TB">${tb.maThietBi}</div>
                <div class="data-cell" data-label="Tên TB">${tb.tenThietBi}</div>
                <div class="data-cell" data-label="Đơn Giá">${tb.donGia}</div>
                <div class="data-cell" data-label="Đền Bù">${tb.denBu}</div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/thietbi/form?maThietBi=${tb.maThietBi}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/thietbi/delete?maThietBi=${tb.maThietBi}" onclick="return confirm('Bạn có muốn xóa?');"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>