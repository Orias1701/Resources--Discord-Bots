<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Chi Tiết Sử Dụng Dịch Vụ"/>
    <jsp:param name="mainContent" value="../chitietdichvu/list-content.jsp"/>
    <jsp:param name="mainScript" value="../chitietdichvu/list-script.jsp"/>
</jsp:include>