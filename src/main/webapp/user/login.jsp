<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            background-color: #e0f7fa; /* 浅蓝色背景 */
            display: flex;
            justify-content: center;
            align-items: center;
        }

        /* 容器样式 */
        .container {
            width: 100%;
            max-width: 400px; /* 最大宽度 */
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            background: white;
        }
        .text-center {
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
        }

        /* 表单样式 */
        form {
            display: flex;
            flex-direction: column;
        }

        form label {
            margin-bottom: 5px;
        }

        form input[type="text"],
        form input[type="password"] {
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        form input[type="radio"] {
            margin-right: 10px;
        }

        form role-radios {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }
        .form-role-radios {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }
        /* 提交按钮样式 */
        form input[type="submit"] {
            padding: 10px;
            border: none;
            border-radius: 4px;
            background-color: #f321b1;
            color: white;
            cursor: pointer;
            margin-top: auto; /* 将按钮推到底部 */
        }

        /* 注册链接样式 */
        .register-link {
            margin-top: 15px;
            text-align: center;
        }

        .register-link a {
            color: #4821f3;
            text-decoration: none;
            font-weight: bold;
        }

        .register-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
        <div class="container text-center">
            <!-- 其他内容保持不变 -->
            <h2>Login</h2>
            <!-- 表单和注册链接 -->
            <form action="${pageContext.request.contextPath}/login" method="post">
            <!-- 用户名和密码输入区域 -->
            <div class="input-group">
                <label for="name">Username:</label>
                <input type="text" id="name" name="name" required><br>

                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <!-- 角色选择区域 -->
            <div class="form-role-radios">
                <input type="radio" id="userRole" name="role" value="user" checked>
                <label for="userRole">User</label>
                <input type="radio" id="adminRole" name="role" value="admin">
                <label for="adminRole">Admin</label>
            </div>

            <!-- 登录按钮 -->
            <input type="submit" value="Login">
            </form>
            <div class="register-link">
                还没有账号？<a href="goRegister">注册</a>
            </div>
        </div>

</body>
</html>