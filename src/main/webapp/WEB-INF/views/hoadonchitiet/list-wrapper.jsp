<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Hóa Đơn Chi Tiết"/>
    <jsp:param name="mainContent" value="../hoadonchitiet/list-content.jsp"/>
    <jsp:param name="mainScript" value="../hoadonchitiet/list-script.jsp"/>
</jsp:include>