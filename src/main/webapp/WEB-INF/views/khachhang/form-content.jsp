<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-container">
    <h2>Khách hàng</h2>

    <form action="/khachhang/save" method="post">
        <div class="form-group">
            <label>Mã khách hàng</label>
            <c:choose>
                <c:when test="${isEdit}">
                    <!-- Hiển thị cho người dùng xem, KHÔNG submit -->
                    <input type="text" value="${khachHang.maKhachHang}" disabled>
                    <!-- Giá trị thực tế được submit -->
                    <input type="hidden" name="maKhachHang" value="${khachHang.maKhachHang}">
                </c:when>
                <c:otherwise>
                    <input type="text" name="maKhachHang" value="${khachHang.maKhachHang}" required>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="form-group">
            <label>Tên khách hàng</label>
            <input type="text" name="tenKhachHang" value="${khachHang.tenKhachHang}" required>
        </div>

        <div class="form-group">
            <label>Giới tính</label>
            <select name="gioiTinh" required>
                <c:forEach var="gioiTinh" items="${gioiTinhList}">
                    <option value="${gioiTinh}" ${khachHang.gioiTinh == gioiTinh ? 'selected' : ''}>${gioiTinh}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Số điện thoại</label>
            <input type="text" name="sdt" value="${khachHang.sdt}" required>
        </div>

        <div class="form-group">
            <label>Tình trạng</label>
            <select name="tinhTrangKhach" required>
                <c:forEach var="tinhTrang" items="${tinhTrangKhachList}">
                    <option value="${tinhTrang}" ${khachHang.tinhTrangKhach == tinhTrang ? 'selected' : ''}>${tinhTrang}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-actions">
            <a href="/khachhang" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">${isEdit ? 'Lưu' : 'Thêm'}</button>
        </div>
    </form>
</div>
