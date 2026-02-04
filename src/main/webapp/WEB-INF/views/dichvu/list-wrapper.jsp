<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thông Tin Dịch Vụ"/>
    <jsp:param name="mainContent" value="../dichvu/list-content.jsp"/>
    <jsp:param name="mainScript" value="../dichvu/list-script.jsp"/>
</jsp:include>