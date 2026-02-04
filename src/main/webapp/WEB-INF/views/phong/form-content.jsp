<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-container">
    <h2>Phòng</h2>
    <form method="post" action="/phong/save">
        <div class="form-group">
            <label>Mã Phòng</label>
            <input type="text" name="maPhong" value="${phong.maPhong}" ${phong.maPhong != null ? "readonly" : ""}/>
        </div>
        <div class="form-group">
            <label>Tên Phòng</label>
            <input type="text" name="tenPhong" value="${phong.tenPhong}"/>
        </div>
        <div class="form-group">
            <label>Loại Phòng</label>
            <select name="maLoai.maLoai">
                <c:forEach var="lp" items="${listLoaiPhong}">
                    <option value="${lp.maLoai}" ${phong.maLoai.maLoai == lp.maLoai ? "selected" : ""}>
                        ${lp.tenLoai}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>Mô Tả</label>
            <input type="text" name="moTa" value="${phong.moTa}"/>
        </div>
        <div class="form-group">
            <label>Tình Trạng Phòng</label>
            <select name="tinhTrangPhong">
                <c:forEach var="tt" items="${tinhTrangOptions}">
                    <option value="${tt}" ${phong.tinhTrangPhong == tt ? "selected" : ""}>${tt}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-actions">
            <a href="/phong" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>