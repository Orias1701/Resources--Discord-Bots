<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Thông Tin Nhân Viên"/>
    <jsp:param name="mainContent" value="../nhanvien/list-content.jsp"/>
    <jsp:param name="mainScript" value="../nhanvien/list-script.jsp"/>
</jsp:include>