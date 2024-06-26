<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>申报项目信息管理</title>
    <!-- 引入Bootstrap CDN，用于更好的样式和响应式布局 -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .table {
            margin-top: 20px;
            border-collapse: collapse;
            width: 90%;
            margin-left: auto;
            margin-right: auto;
        }
        .table th, .table td {
            border: 1px solid #ddd;
            padding: 8px;
        }
        .table th {
            padding-top: 12px;
            padding-bottom: 12px;
            text-align: left;
            background-color: #f2f2f2;
            color: black;
        }
        .center-text {
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <h1 class="center-text">项目列表</h1>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>编号</th>
            <th>项目名称</th>
            <th>申报开始日期</th>
            <th>申报结束日期</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${project.id}</td>
            <td>${project.name}</td>
            <td><fmt:formatDate value="${project.startDate}" pattern="yyyy-MM-dd" /></td>
            <td><fmt:formatDate value="${project.endDate}" pattern="yyyy-MM-dd" /></td>
            <td>${project.status}</td>
        </tr>
        </tbody>
    </table>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
