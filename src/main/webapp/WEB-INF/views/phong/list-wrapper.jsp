<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Phòng"/>
    <jsp:param name="mainContent" value="../phong/list-content.jsp"/>
    <jsp:param name="mainScript" value="../phong/list-script.jsp"/>
</jsp:include>