<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<html>
<head>
    <title>Employee List</title>
    <style>
        table {
            border-collapse: collapse;
            width: 80%;
        }
        th, td {
            padding: 8px;
            border: 1px solid #333;
            text-align: center;
        }
        a.btn {
            padding: 5px 10px;
            border-radius: 4px;
            text-decoration: none;
            margin: 2px;
        }
        .add { background: green; color: white; }
        .edit { background: orange; color: white; }
        .delete { background: red; color: white; }
    </style>
</head>
<body>
<h2>Employee List</h2>
<a class="btn add" href="${pageContext.request.contextPath}/nhanvien/add">Add Employee</a>
<table>
    <tr>
        <th>Employee ID</th>
        <th>Name</th>
        <th>Phone</th>
        <th>Gender</th>
        <th>Position</th>
        <th>Role</th>
        <th>Action</th>
    </tr>
    <c:forEach var="nv" items="${listNhanVien}">
        <tr>
            <td>${nv.maNhanVien}</td>
            <td>${nv.tenNhanVien}</td>
            <td>${nv.sdt}</td>
            <td>${nv.gioiTinh}</td>
            <td>${nv.chucVu}</td>
            <td>
                <c:choose>
                    <c:when test="${nv.user != null}">
                        ${nv.user.role.name}
                    </c:when>
                    <c:otherwise>
                        N/A
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a class="btn edit" href="${pageContext.request.contextPath}/nhanvien/edit/${nv.maNhanVien}">Edit</a>
                <a class="btn delete" href="${pageContext.request.contextPath}/nhanvien/delete/${nv.maNhanVien}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
