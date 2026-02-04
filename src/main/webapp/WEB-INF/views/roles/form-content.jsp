<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="form-container">
    <h2>Phân Quyền</h2>
    <form action="${pageContext.request.contextPath}/roles/save" method="post">
        <input type="hidden" name="id" value="${role.id}" />
        <div class="form-group">
            <label>Tên Role</label>
            <input type="text" name="name" value="${role.name}" required/>
        </div>
        <div class="form-group">
            <label>Mô tả</label>
            <input type="text" name="description" value="${role.description}"/>
        </div>
        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/roles" class="btn-secondary">Quay lại</a>
            <button type="submit" class="btn-primary">Lưu</button>
        </div>
    </form>
</div>