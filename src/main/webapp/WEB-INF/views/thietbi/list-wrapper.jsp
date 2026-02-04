<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thiết bị"/>
    <jsp:param name="mainContent" value="../thietbi/list-content.jsp"/>
    <jsp:param name="mainScript" value="../thietbi/list-script.jsp"/>
</jsp:include>