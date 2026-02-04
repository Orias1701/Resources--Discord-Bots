<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Form Phòng</h2>

<form method="post" action="/phong/save">
    <div>
        <label>Mã Phòng:</label>
        <input type="text" name="maPhong" value="${phong.maPhong}" ${phong.maPhong != null ? "readonly" : ""}/>
    </div>
    <div>
        <label>Tên Phòng:</label>
        <input type="text" name="tenPhong" value="${phong.tenPhong}"/>
    </div>
    <div>
        <label>Loại Phòng:</label>
        <select name="maLoai.maLoai">
            <c:forEach var="lp" items="${listLoaiPhong}">
                <option value="${lp.maLoai}" ${phong.maLoai != null && phong.maLoai.maLoai == lp.maLoai ? "selected" : ""}>
                    ${lp.tenLoai}
                </option>
            </c:forEach>
        </select>
    </div>
    <div>
        <label>Mô Tả:</label>
        <input type="text" name="moTa" value="${phong.moTa}"/>
    </div>
    <div>
        <label>Tình Trạng Phòng:</label>
        <select name="tinhTrangPhong">
            <c:forEach var="tt" items="${tinhTrangOptions}">
                <option value="${tt}" ${phong.tinhTrangPhong == tt ? "selected" : ""}>${tt}</option>
            </c:forEach>
        </select>
    </div>
    <div>
        <button type="submit">Lưu</button>
    </div>
</form>