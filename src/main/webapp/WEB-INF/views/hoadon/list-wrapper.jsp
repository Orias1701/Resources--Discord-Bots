<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thông Tin Hóa Đơn"/>
    <jsp:param name="mainContent" value="../hoadon/list-content.jsp"/>
    <jsp:param name="mainScript" value="../hoadon/list-script.jsp"/>
</jsp:include>