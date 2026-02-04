<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thông Tin Tài Khoản"/>
    <jsp:param name="mainContent" value="../users/list-content.jsp"/>
    <jsp:param name="mainScript" value="../users/list-script.jsp"/>
</jsp:include>