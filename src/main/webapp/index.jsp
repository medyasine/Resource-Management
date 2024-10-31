<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Index Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        h1 {
            color: #333;
        }

        ul {
            list-style-type: none;
            padding: 0;
        }

        li {
            margin: 10px 0;
        }

        a {
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h1>Welcome to the Product and Category Management System</h1>
<ul>
    <li><a href="${pageContext.request.contextPath}/product-servlet?actionKey=list">Manage Products</a></li>
    <li><a href="${pageContext.request.contextPath}/category-servlet?actionKey=list">Manage Categories</a></li>
</ul>
</body>
</html>