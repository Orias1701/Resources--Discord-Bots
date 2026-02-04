<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thiết bị Phòng"/>
    <jsp:param name="mainContent" value="../thietbiphong/list-content.jsp"/>
    <jsp:param name="mainScript" value="../thietbiphong/list-script.jsp"/>
</jsp:include>