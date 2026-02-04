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
    <title>Danh sach Loai Phong</title>
</head>
<body>
<h2>Danh sach Loai Phong</h2>

<form method="get" action="/loaiphong">
    Tim kiem: <input type="text" name="keyword" />
    <button type="submit">Tim</button>
</form>

<form method="get" action="/loaiphong">
    Loc theo gia: tu <input type="text" name="minPrice" /> den <input type="text" name="maxPrice" />
    <button type="submit">Loc</button>
</form>

<a href="/loaiphong/form">Them moi Loai Phong</a>

<table border="1" cellpadding="5">
    <tr>
        <th>Ma Loai</th>
        <th>Ten Loai</th>
        <th>So Giuong</th>
        <th>Gia Loai</th>
        <th>Hanh dong</th>
    </tr>
    <c:forEach var="lp" items="${list}">
        <tr>
            <td>${lp.maLoai}</td>
            <td>${lp.tenLoai}</td>
            <td>${lp.soGiuong}</td>
            <td>${lp.giaLoai}</td>
            <td>
                <a href="/loaiphong/form?maLoai=${lp.maLoai}">Sua</a>
                <a href="/loaiphong/delete?maLoai=${lp.maLoai}" onclick="return confirm('Ban co muon xoa?');">Xoa</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
