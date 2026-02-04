<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-list.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Kiểm Tra Phòng"/>
    <jsp:param name="mainContent" value="../kiemtraphong/list-content.jsp"/>
    <jsp:param name="mainScript" value="../kiemtraphong/list-script.jsp"/>
</jsp:include>