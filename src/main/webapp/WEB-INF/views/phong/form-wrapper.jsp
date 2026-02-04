<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/views/.common/layout-form.jsp">
    <jsp:param name="pageTitle" value="Quản Lý Phòng"/>
    <jsp:param name="mainContent" value="../phong/form-content.jsp"/>
    <jsp:param name="pageScript" value="../phong/form-script.jsp"/>
</jsp:include>