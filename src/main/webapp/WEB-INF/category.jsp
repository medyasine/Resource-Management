<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.categoryproduct.model.Category" %>
<%@ page import="com.example.categoryproduct.formbean.formCategory" %>
<%@ page import="java.util.List" %>
<%
    List<Category> categories = (List<Category>) session.getAttribute("categories");
    formCategory formCategory = (formCategory) request.getSession().getAttribute("category");
    boolean hasCategory = formCategory != null;
%>
<html>
<head>
    <title>Category Management</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            padding: 20px;
            background-color: #f4f4f4;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            border-radius: 5px;
        }

        h2 {
            color: #333;
            margin-bottom: 20px;
        }

        .form-container {
            background-color: #fff;
            padding: 20px;
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #666;
        }

        .form-group input[type="text"],
        .form-group textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        .btn {
            padding: 8px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            margin-right: 10px;
        }

        .btn-primary {
            background-color: #007bff;
            color: white;
        }

        .btn-danger {
            background-color: #dc3545;
            color: white;
        }

        .btn-warning {
            background-color: #ffc107;
            color: #000;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f8f9fa;
            font-weight: bold;
        }

        tr:hover {
            background-color: #f5f5f5;
        }

        .action-buttons {
            display: flex;
            gap: 5px;
        }

        .nav-links {
            margin-bottom: 20px;
        }

        .nav-links a {
            margin-right: 15px;
            text-decoration: none;
            color: #007bff;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="nav-links">
        <a href="product-servlet">Products</a>
        <a href="category-servlet">Categories</a>
    </div>

    <h2>Category Management</h2>

    <!-- Category Form -->
    <div class="form-container">
        <form action="category-servlet" method="post">
            <input type="hidden" name="actionKey" value="<%= hasCategory && formCategory.getId() > 0 ? "update" : "add" %>">
            <input type="hidden" name="id" value="<%= hasCategory ? formCategory.getId() : "" %>">

            <div class="form-group">
                <label for="nom">Name:</label>
                <input type="text" id="nom" name="nom" value="<%= hasCategory ? formCategory.getNom() : "" %>" required>
            </div>

            <div class="form-group">
                <label for="description">Description:</label>
                <textarea id="description" name="description" rows="3" required><%= hasCategory ? formCategory.getDescription() : "" %></textarea>
            </div>

            <button type="submit" class="btn btn-primary">
                <%= hasCategory && formCategory.getId() > 0 ? "Update" : "Add" %> Category
            </button>
            <% if (hasCategory && formCategory.getId() > 0) { %>
            <a href="category-servlet" class="btn btn-warning">Cancel</a>
            <% } %>
        </form>
    </div>

    <!-- Search Form -->
    <div class="form-group">
        <label for="search">Search:</label>
        <form action="category-servlet" method="get">
            <input type="hidden" name="actionKey" value="filter">
            <input type="text" id="search" name="filter" class="form-control" placeholder="Search categories">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>

    <!-- Categories Table -->
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            if(categories != null) {
                for(Category category : categories) {
        %>
        <tr>
            <td><%=category.getId()%></td>
            <td><%=category.getNom()%></td>
            <td><%=category.getDescription()%></td>
            <td class="action-buttons">
                <a href="category-servlet?actionKey=edit&id=<%=category.getId()%>"
                   class="btn btn-warning">Edit</a>
                <form action="category-servlet" method="post" style="display: inline;">
                    <input type="hidden" name="actionKey" value="delete">
                    <input type="hidden" name="id" value="<%=category.getId()%>">
                    <button type="submit" class="btn btn-danger"
                            onclick="return confirm('Are you sure you want to delete this category? This will also delete all associated products.')">
                        Delete
                    </button>
                </form>
            </td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>