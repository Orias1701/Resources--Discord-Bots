<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty error}">
  <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty message}">
  <div class="alert alert-success">${message}</div>
</c:if>

<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sach Dich Vu</title>
</head>
<body>
<h2>Danh sach Dich Vu</h2>

<form method="get" action="/dichvu">
    Tim kiem: <input type="text" name="keyword" />
    <button type="submit">Tim</button>
</form>

<form method="get" action="/dichvu">
    Loc theo gia: tu <input type="text" name="minPrice" /> den <input type="text" name="maxPrice" />
    <button type="submit">Loc</button>
</form>

<a href="/dichvu/form">Them moi Dich Vu</a>

<table border="1" cellpadding="5">
    <tr>
        <th>Ma Dich Vu</th>
        <th>Ten Dich Vu</th>
        <th>Loai Dich Vu</th>
        <th>Gia Dich Vu</th>
        <th>Hanh dong</th>
    </tr>
    <c:forEach var="dv" items="${list}">
        <tr>
            <td>${dv.maDichVu}</td>
            <td>${dv.tenDichVu}</td>
            <td>${dv.loaiDichVu}</td>
            <td>${dv.giaDichVu}</td>
            <td>
                <a href="/dichvu/form?maDichVu=${dv.maDichVu}">Sua</a>
                <a href="/dichvu/delete?maDichVu=${dv.maDichVu}" onclick="return confirm('Ban co muon xoa?');">Xoa</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
