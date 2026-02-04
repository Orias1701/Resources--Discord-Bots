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
    <title>Form thiet bi</title>
</head>
<body>
<h2>Form thiet bi</h2>

<form method="post" action="/thietbi/save">
    MaThietBi:
    <input type="text" name="maThietBi" value="${tb.maThietBi}"
    ${tb.maThietBi != null ? "readonly" : ""} /> <br/>

    TenThietBi:
    <input type="text" name="tenThietBi" value="${tb.tenThietBi}" /> <br/>

    DonGia:
    <input type="text" name="donGia" value="${tb.donGia}" /> <br/>

    DenBu:
    <input type="text" name="denBu" value="${tb.denBu}" /> <br/>

    <button type="submit">Luu</button>
    <a href="/thietbi">Huy</a>
</form>

</body>
</html>
