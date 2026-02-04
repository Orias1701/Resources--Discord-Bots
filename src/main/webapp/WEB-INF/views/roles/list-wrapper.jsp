<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thông Tin Phân Quyền"/>
    <jsp:param name="mainContent" value="../roles/list-content.jsp"/>
    <jsp:param name="mainScript" value="../roles/list-script.jsp"/>
</jsp:include>