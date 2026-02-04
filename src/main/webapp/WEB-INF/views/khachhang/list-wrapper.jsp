<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thông Tin Khách Hàng"/>
    <jsp:param name="mainContent" value="../khachhang/list-content.jsp"/>
    <jsp:param name="mainScript" value="../khachhang/list-script.jsp"/>
</jsp:include>