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
    <title>Form Dich Vu</title>
</head>
<body>
<h2>Form Dich Vu</h2>

<form method="post" action="/dichvu/save">
    Ma Dich Vu: <input type="text" name="maDichVu" value="${dichvu.maDichVu}" ${dichvu.maDichVu != null ? "readonly" : ""} /> <br/>
    Ten Dich Vu: <input type="text" name="tenDichVu" value="${dichvu.tenDichVu}" /> <br/>
    Loai Dich Vu: <input type="text" name="loaiDichVu" value="${dichvu.loaiDichVu}" /> <br/>
    Gia Dich Vu: <input type="text" name="giaDichVu" value="${dichvu.giaDichVu}" /> <br/>
    <button type="submit">Luu</button>
    <a href="/dichvu">Huy</a>
</form>

</body>
</html>
