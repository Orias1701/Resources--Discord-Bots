<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="form-container">
    <h2>Quản Lý Tài Khoản</h2>
    <form action="/users/save" method="post">
        <input type="hidden" name="id" value="${user.id}" />

        <div class="form-group">
            <label>Tên đăng nhập (Username)</label>
            <input type="text" name="username" value="${user.username}" required/>
        </div>

        <div class="form-group">
            <label>Mật khẩu (Password)</label>
            <input type="password" name="password" required/>
        </div>

        <div class="form-group">
            <label>Vai trò (Role)</label>
            <select name="role.id" required>
                <c:forEach var="r" items="${roles}">
                    <option value="${r.id}" <c:if test="${user.role.id == r.id}">selected</c:if>>
                        ${r.name}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>Nhân viên:</label>
            <input type="text" value="${user.nhanVien.maNhanVien}" readonly/>
            <input type="hidden" name="nhanVien.maNhanVien" value="${user.nhanVien.maNhanVien}" />
        </div>



        <div class="form-actions">
            <a href="/users" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>