<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <title>Danh sach Su Dung Dich Vu</title>
</head>
<body>
<h2>Danh sach Su Dung Dich Vu</h2>

<!-- Flash messages -->
<c:if test="${not empty message}">
    <div style="color: green;"><c:out value="${message}"/></div>
</c:if>
<c:if test="${not empty error}">
    <div style="color: red;"><c:out value="${error}"/></div>
</c:if>

<!-- Form tim kiem theo keyword -->
<form method="get" action="/sddv">
    Tim kiem: <input type="text" name="keyword" value="<c:out value='${keyword}'/>" />
    <button type="submit">Tim</button>
</form>

<!-- Form loc theo khoang ngay -->
<form method="get" action="/sddv" style="margin-top:8px;">
    Loc theo ngay:
    tu <input type="date" name="startDate" value="${startDate}" />
    den <input type="date" name="endDate" value="${endDate}" />
    <button type="submit">Loc</button>
</form>

<p style="margin-top:8px;">
    <a href="/sddv/form">Them moi Su Dung Dich Vu</a>
</p>

<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>Ma SDDV</th>
        <th>Khach Hang</th>
        <th>Ma Nhan Vien</th>
        <th>Ngay SDDV</th>
        <th>Thanh Toán</th>
        <th>Tổng tiền</th>
        <th>Hanh dong</th>
    </tr>
    <c:forEach var="sddv" items="${list}">
        <tr>
            <td><c:out value="${sddv.maSDDV}"/></td>
            <td><c:out value="${sddv.khachHang != null ? sddv.khachHang.tenKhachHang : ''}"/></td>
            <td><c:out value="${sddv.maNhanVien != null? sddv.maNhanVien.maNhanVien: ''}"/></td>
            <td><c:out value="${sddv.ngaySDDV}"/></td>
            <td><c:out value="${sddv.thanhToan}"/></td>
            <td>
                <c:choose>
                    <c:when test="${sddv.tongTien != null}">
                        <fmt:formatNumber value="${sddv.tongTien}" type="number"/>
                    </c:when>
                    <c:otherwise>0</c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="/sddv/form?maSDDV=${sddv.maSDDV}">Sua</a>
                &nbsp;|&nbsp;
                <a href="/sddv/delete?maSDDV=${sddv.maSDDV}" onclick="return confirm('Ban co muon xoa?');">Xoa</a>
            </td>
        </tr>
    </c:forEach>

    <c:if test="${empty list}">
        <tr><td colspan="6" style="text-align:center;">Không có dữ liệu</td></tr>
    </c:if>
</table>
</body>
</html>
