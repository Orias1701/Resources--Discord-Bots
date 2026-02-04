<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Form</title>
</head>
<body>
<h2>
    <c:if test="${user.id == null}">Add User</c:if>
    <c:if test="${user.id != null}">Edit User</c:if>
</h2>

<form action="${pageContext.request.contextPath}/users/save" method="post">
    <input type="hidden" name="id" value="${user.id}" />

    <label>Username:</label>
    <input type="text" name="username" value="${user.username}" required/><br/><br/>

    <label>Password:</label>
    <input type="password" name="password" value="${user.password}" required/><br/><br/>

    <label>Role:</label>
    <select name="role.id" required>
        <c:forEach var="r" items="${roles}">
            <option value="${r.id}"
                    <c:if test="${user.role != null && user.role.id == r.id}">selected</c:if>>
                    ${r.name}
            </option>
        </c:forEach>
    </select><br/><br/>

    <label>Chon Nhan Vien</label>
    <select name="nhanVien.maNhanVien" required>
        <c:forEach var="nv" items="${nhanViens}">
            <option value="${nv.maNhanVien}"
                ${user.nhanVien != null && user.nhanVien.maNhanVien == nv.maNhanVien ? 'selected' : ''}>
                    ${nv.tenNhanVien}
            </option>
        </c:forEach>
    </select>


    <input type="submit" value="Save" />
</form>

<br/>
<a href="${pageContext.request.contextPath}/users">← Back</a>
</body>
</html>
