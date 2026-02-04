<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Đăng nhập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <style>
        /* CSS tùy chỉnh để ghi đè và bổ sung Bootstrap */
        .bg-container {
            background-image: url('https://images.pexels.com/photos/258154/pexels-photo-258154.jpeg');
            background-position: center;
            background-repeat: no-repeat;
            background-size: cover;
            position: relative;
        }
        .bg-container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0, 0, 0, 0.6);
        }
        .content-above-overlay { position: relative; z-index: 2; }
        .logo { font-size: 1rem; font-weight: bold; letter-spacing: 2px; }
        .logo-img { width: 90px; height: auto; }
        .form-control { border-radius: 8px; border: none; }
        .form-control:focus {
            box-shadow: 0 0 0 0.25rem rgba(255, 255, 255, 0.25);
            background-color: #fff;
            border-color: #fff;
        }
        .form-control-custom { padding-top: 0.5rem; padding-bottom: 0.5rem; }
        .btn-login {
            background-color: #f0f0f0;
            color: #333;
            font-weight: bold;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }
        .btn-login:hover { background-color: #ddd; color: #333; }
        .logo span {
            font-weight: bold;
            letter-spacing: 3px;
            text-transform: uppercase;
            position: relative;
            top: -38px;
            right: -20px;
        }
        .pass { font-weight: 700; }
        .user { font-weight: 700; }
    </style>
</head>
<body>

<div class="bg-container min-vh-100 d-flex justify-content-center align-items-center">
    <div class="logo position-absolute top-0 start-0 p-4 text-white content-above-overlay d-flex flex-column align-items-center">
        <img src="${pageContext.request.contextPath}/album/logo.svg" alt="Flarista Logo" class="logo-img">
        <span class="mt-2">FLARISTA</span>
    </div>

    <div class="container content-above-overlay">
        <div class="row justify-content-center">
            <div class="col-11 col-md-7 col-lg-5">

                <div class="text-center text-white">
                    <h2 class="text-uppercase mb-4" style="letter-spacing: 3px;">Đăng nhập</h2>



                    <form action="${pageContext.request.contextPath}/doLogin" method="post">
                        <div class="mb-3 row align-items-center user">
                            <label for="username" class="col-md-4 col-form-label text-md-start">Tên người dùng:</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control form-control-custom" id="username" name="username" required>
                            </div>
                        </div>

                        <div class="mb-4 row align-items-center pass">
                            <label for="password" class="col-md-4 col-form-label text-md-start">Mật khẩu:</label>
                            <div class="col-md-8">
                                <input type="password" class="form-control form-control-custom" id="password" name="password" required>
                            </div>
                        </div>

                        <!-- Hiển thị lỗi hoặc thông báo logout -->
                        <c:if test="${param.error != null}">
                            <div class="alert alert-danger" role="alert">
                                Sai tài khoản hoặc mật khẩu!
                            </div>
                        </c:if>
                        <c:if test="${param.logout != null}">
                            <div class="alert alert-success" role="alert">
                                Bạn đã đăng xuất thành công!
                            </div>
                        </c:if>

                        <div class="text-center">
                            <button type="submit" class="btn btn-login px-5 mt-3">Đăng nhập</button>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>
