<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-container">
    <h2>Loại Phòng</h2>
    <form method="post" action="/loaiphong/save">
        <div class="form-group">
            <label>Mã Loại</label>
            <input type="text" name="maLoai" value="${loaiphong.maLoai}" ${loaiphong.maLoai != null ? "readonly" : ""} />
        </div>
        <div class="form-group">
            <label>Tên Loại</label>
            <input type="text" name="tenLoai" value="${loaiphong.tenLoai}" />
        </div>
        <div class="form-group">
            <label>Số Giường</label>
            <input type="number" name="soGiuong" value="${loaiphong.soGiuong}" />
        </div>
        <div class="form-group">
            <label>Giá Loại</label>
            <input type="number" name="giaLoai" value="${loaiphong.giaLoai}" />
        </div>
        <div class="form-actions">
            <a href="/loaiphong" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>