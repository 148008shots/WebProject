<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>申报项目信息管理</title>
    <style>
        body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 0;
    }
        .header {
            background-color: #363233;
            color: white;
            text-align: center;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 30px;
            margin-bottom: 20px; /* 增加底部间距 */
        }
    h1 {
        margin: 0;
    }
    table {
        width: 80%;
        margin: 20px auto;
        border-collapse: collapse;
    }
    th, td {
        padding: 8px;
        text-align: left;
        border-bottom: 1px solid #ddd;
    }
    th {
        background-color: #4CAF50;
        color: white;
    }
    tr:hover {
        background-color: #f5f5f5;
    }
    a {
        color: #333;
        text-decoration: none;
    }
    a:hover {
        text-decoration: underline;
    }
    .sidebar {
        position: fixed;
        top: 0;
        left: 0;
        width: 180px;
        height: 100%;
        background-color: #363233;
        overflow-x: hidden;
        padding-top:50px;
    }
    .sidebar a {
        padding: 6px 8px 6px 16px;
        text-decoration: none;
        font-size: 18px; /* 更改字体大小 */
        color: #f1f1f1;
        display: block;

    }
    .sidebar a:hover {
        color: #f1f1f1;
    }
    /* 调整主体内容的样式，使其不会被侧边栏遮挡 */
        .content {
            margin-left: 150px; /* 减少左侧间距 */
            padding: 0px 16px;
            height: 1000px;
        }
    .welcome {
        font-size: 16px;
    }
    .logout {
        font-size: 16px;
        color: white;
        text-decoration: none;
    }
    .logout:hover {
        text-decoration: underline;
    }
    .sidebar-header {
        font-size: 15px; /* 更改字体大小 */
        color: white;
        text-align: center;
        margin-bottom: 20px;
    }
    .audit-link {
        color: cornflowerblue;
    }
    </style>
</head>
<body>
<div class="sidebar">
    <div class="sidebar-header">申报项目信息管理系统</div>
    <a href="index.jsp">首页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;></a>
    <a href="../WEB-INF/jsp/management.jsp">项目管理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;></a> <%--灵活管理项目，可以及时添加和修改项目信息--%>
    <a href="../WEB-INF/jsp/apply.jsp">审核管理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;></a> <%--对申请的项目进行审核，可以进行退回和通过选项，需要添加信息查询功能--%>
    <a href="../WEB-INF/jsp/new.jsp">新闻管理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;></a> <%-- 发布各类新闻信息--%>
</div>

<div class="content">
    <div class="header">
        欢迎，某某某：<span>${username}</span>
        <div class="welcome">
            <a href="logout.jsp" class="logout">退出登录</a>
        </div>
    </div>

    <table>
        <tr>
            <th>编号</th>
            <th>项目名称</th>
            <th>申报开始日期</th>
            <th>申报结束日期</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        <c:forEach var="project" items="${projects}">
            <tr>
                <td>${project.id}</td>
                <td>${project.name}</td>
                <td><fmt:formatDate value="${project.startDate}" pattern="yyyy-MM-dd" /></td>
                <td><fmt:formatDate value="${project.endDate}" pattern="yyyy-MM-dd" /></td>
                <td>
                    <c:choose>
                        <c:when test="${project.status eq '0'}">已申报</c:when>
                        <c:when test="${project.status eq '1'}">审核中
                        </c:when>
                        <c:when test="${project.status eq '2'}">已审核</c:when>
                        <c:otherwise>未知状态</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${project.status ne '2'}">
                            <a href="projectDetail.jsp?id=${project.id}" class="audit-link">审核</a>
                        </c:when>
                        <c:otherwise>
                            <!-- 已审核状态的处理逻辑 -->
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
