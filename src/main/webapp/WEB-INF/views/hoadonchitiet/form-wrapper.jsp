<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-form.jsp">
    <jsp:param name="pageTitle" value="Hóa Đơn Chi Tiết"/>
    <jsp:param name="mainContent" value="../hoadonchitiet/form-content.jsp"/>
    <jsp:param name="pageScript" value="../hoadonchitiet/form-script.jsp"/>
</jsp:include>