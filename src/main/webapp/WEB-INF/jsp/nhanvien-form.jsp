<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<html>
<head>
    <title>Employee Form</title>
    <style>
        input[readonly] {
            background-color: #e0e0e0;
            color: #555;
        }
    </style>
</head>
<body>
<h2>Employee Form</h2>
<form:form action="${pageContext.request.contextPath}/nhanvien/save" method="post" modelAttribute="nhanVien">
    <table>
        <tr>
            <td>Employee ID:</td>
            <td><form:input path="maNhanVien" readonly="true"/></td>
        </tr>
        <tr>
            <td>Name:</td>
            <td><form:input path="tenNhanVien"/></td>
        </tr>
        <tr>
            <td>Phone:</td>
            <td><form:input path="sdt"/></td>
        </tr>
        <tr>
            <td>Gender:</td>
            <td>
                <form:select path="gioiTinh">
                    <form:option value="NAM">NAM</form:option>
                    <form:option value="NU">NU</form:option>
                </form:select>
            </td>
        </tr>
        <tr>
            <td>Position:</td>
            <td><form:input path="chucVu"/></td>
        </tr>
        <tr>
            <td>Role:</td>
            <td>
                <select name="roleId">
                    <c:forEach items="${roles}" var="role">
                        <option value="${role.id}">${role.name}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Save"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>