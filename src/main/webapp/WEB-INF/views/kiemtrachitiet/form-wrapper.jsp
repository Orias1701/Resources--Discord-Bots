<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-form.jsp">
    <jsp:param name="pageTitle" value="Chi Tiết Kiểm Tra"/>
    <jsp:param name="mainContent" value="../kiemtrachitiet/form-content.jsp"/>
    <jsp:param name="pageScript" value="../kiemtrachitiet/form-script.jsp"/>
</jsp:include>