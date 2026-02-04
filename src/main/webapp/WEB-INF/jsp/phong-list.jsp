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
    <title>Danh sach Phong</title>
</head>
<body>
<h2>Danh sach Phong</h2>

<form method="get" action="/phong">
    Tim kiem: <input type="text" name="keyword" />
    <button type="submit">Tim</button>
</form>

<form method="get" action="/phong">
    Loc theo tinh trang:
    <select name="tinhTrang">
        <option value="">Tat ca</option>
        <c:forEach var="tt" items="${tinhTrangOptions}">
            <option value="${tt}">${tt}</option>
        </c:forEach>
    </select>
    <button type="submit">Loc</button>
</form>

<a href="/phong/form">Them moi Phong</a>

<table border="1" cellpadding="5">
    <tr>
        <th>Ma Phong</th>
        <th>Ten Phong</th>
        <th>Loai Phong</th>
        <th>Mo Ta</th>
        <th>Tinh Trang</th>
        <th>Hanh dong</th>
    </tr>
    <c:forEach var="p" items="${list}">
        <tr>
            <td>${p.maPhong}</td>
            <td>${p.tenPhong}</td>
            <td>${p.maLoai.tenLoai}</td>
            <td>${p.moTa}</td>
            <td>
                <select class="tinhTrangSelect" data-maphong="${p.maPhong}">
                    <c:forEach var="tt" items="${tinhTrangOptions}">
                        <option value="${tt}" ${p.tinhTrangPhong == tt ? "selected" : ""}>${tt}</option>
                    </c:forEach>
                </select>
            </td>
            <td>
                <a href="/phong/form?maPhong=${p.maPhong}">Sua</a>
                <a href="/phong/delete?maPhong=${p.maPhong}" onclick="return confirm('Ban co muon xoa?');">Xoa</a>
            </td>
        </tr>
    </c:forEach>
</table>

<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script>
    $(document).ready(function() {
        $('.tinhTrangSelect').change(function() {
            var maPhong = $(this).data('maphong');
            var tinhTrangMoi = $(this).val();
            $.ajax({
                url: '/phong/updateTinhTrang',
                method: 'POST',
                data: { maPhong: maPhong, tinhTrangPhong: tinhTrangMoi },
                success: function() {
                    alert('Cap nhat thanh cong!');
                },
                error: function() {
                    alert('Cap nhat that bai!');
                }
            });
        });
    });
</script>

</body>
</html>
