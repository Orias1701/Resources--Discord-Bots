<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h2>Danh sách Nhân Viên</h2>
</div>

<div id = "sw_layout">
    <span id = "sw_table">Bảng</span>
    <span id = "sw_card">Thẻ</span>
</div>

<div class="page-controls">
    <div class="control-group filters">
        <form method="get" action="/nhanvien">
            <input type="text" name="keyword" placeholder="Tìm kiếm..." value="${param.keyword}"/>
            <button type="submit"><i class="fa-regular fa-circle"></i></button>
            <a href="/nhanvien" class="btn-secondary"><i class="fa-solid fa-arrow-rotate-left"></i></a>
        </form>
    </div>
    <div class="control-group actions">
         <a href="javascript:void(0)" onclick="loadForm('/nhanvien/add')" class="btn-primary"><i class="fa-solid fa-plus"></i></a>
    </div>
</div>

<div class="data-layout">
    <div class="data-container table-lyo">
        <c:forEach var="nv" items="${listNhanVien}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã NV">${nv.maNhanVien}</div>
                <div class="data-cell" data-label="Tên NV">${nv.tenNhanVien}</div>
                <div class="data-cell" data-label="SĐT">${nv.sdt}</div>
                <div class="data-cell" data-label="Giới tính">${nv.gioiTinh}</div>
                <div class="data-cell" data-label="Chức vụ">${nv.chucVu}</div>
                <div class="data-cell" data-label="Vai trò">
                    <c:if test="${nv.user != null}">${nv.user.role.name}</c:if>
                    <c:if test="${nv.user == null}">Chưa có</c:if>
                </div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/nhanvien/edit/${nv.maNhanVien}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/nhanvien/delete/${nv.maNhanVien}" onclick="return confirm('Bạn chắc chắn muốn xóa?')"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="data-container grid-lyo">
        <c:forEach var="nv" items="${listNhanVien}">
            <div class="data-item">
                <div class="data-cell" data-label="Mã NV">${nv.maNhanVien}</div>
                <div class="data-cell" data-label="Tên NV">${nv.tenNhanVien}</div>
                <div class="data-cell" data-label="SĐT">${nv.sdt}</div>
                <div class="data-cell" data-label="Giới tính">${nv.gioiTinh}</div>
                <div class="data-cell" data-label="Chức vụ">${nv.chucVu}</div>
                <div class="data-cell" data-label="Vai trò">
                    <c:if test="${nv.user != null}">${nv.user.role.name}</c:if>
                    <c:if test="${nv.user == null}">Chưa có</c:if>
                </div>
                <div class="item-actions">
                    <a href="javascript:void(0)" onclick="loadForm('/nhanvien/edit/${nv.maNhanVien}')"><i class="fa-sharp-duotone fa-solid fa-pen-to-square"></i></a>
                    <a href="/nhanvien/delete/${nv.maNhanVien}" onclick="return confirm('Bạn chắc chắn muốn xóa?')"><i class="fa-solid fa-xmark"></i></a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>