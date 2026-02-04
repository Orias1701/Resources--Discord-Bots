<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thông Tin Loại Phòng"/>
    <jsp:param name="mainContent" value="../loaiphong/list-content.jsp"/>
    <jsp:param name="mainScript" value="../loaiphong/list-script.jsp"/>
</jsp:include>