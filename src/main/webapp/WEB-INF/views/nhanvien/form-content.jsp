<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="form-container">
    <h2>Nhân Viên</h2>
    <form action="${pageContext.request.contextPath}/nhanvien/save" method="post">
        <input type="hidden" name="maNhanVien" value="${nhanVien.maNhanVien}"/>
        <div class="form-group">
            <label>Mã Nhân Viên</label>
            <input type="text" value="${nhanVien.maNhanVien}" readonly/>
        </div>
        <div class="form-group">
            <label>Tên Nhân Viên</label>
            <input type="text" name="tenNhanVien" value="${nhanVien.tenNhanVien}"/>
        </div>
        <div class="form-group">
            <label>Số Điện Thoại</label>
            <input type="text" name="sdt" value="${nhanVien.sdt}"/>
        </div>
        <div class="form-group">
            <label>Giới Tính</label>
            <select name="gioiTinh">
                <option value="NAM" ${nhanVien.gioiTinh == 'NAM' ? 'selected' : ''}>NAM</option>
                <option value="NU" ${nhanVien.gioiTinh == 'NU' ? 'selected' : ''}>NỮ</option>
            </select>
        </div>
        <div class="form-group">
            <label>Chức Vụ</label>
            <input type="text" name="chucVu" value="${nhanVien.chucVu}"/>
        </div>
        <div class="form-group">
            <label>Vai trò (Tài khoản)</label>
            <select name="roleId">
                <c:forEach items="${roles}" var="role">
                    <option value="${role.id}" ${nhanVien.user.role.id == role.id ? 'selected' : ''}>
                        ${role.name}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-actions">
            <a href="/nhanvien" class="btn-secondary">Hủy</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>