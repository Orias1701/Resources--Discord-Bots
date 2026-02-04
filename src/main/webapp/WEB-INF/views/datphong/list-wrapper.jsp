<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thông Tin Đặt Phòng"/>
    <jsp:param name="mainContent" value="../datphong/list-content.jsp"/>
    <jsp:param name="mainScript" value="../datphong/list-script.jsp"/>
</jsp:include>