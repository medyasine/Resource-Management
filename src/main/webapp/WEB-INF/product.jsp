<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.categoryproduct.model.Product" %>
<%@ page import="com.example.categoryproduct.model.Category" %>
<%@ page import="com.example.categoryproduct.formbean.formProduct" %>
<%@ page import="java.util.List" %>
<%
    List<Product> products = (List<Product>) session.getAttribute("products");
    List<Category> categories = (List<Category>) session.getAttribute("categories");
    formProduct formProduct = (formProduct) request.getSession().getAttribute("product");
    boolean hasProduct = formProduct != null;

%>
<html>
<head>
    <title>Product Management</title>
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
        .form-group input[type="number"],
        .form-group textarea,
        .form-group select {
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
        }+

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
    </style>
</head>
<body>
<div class="container">
    <div class="nav-links">
        <a href="product-servlet">Products</a>
        <a href="category-servlet">Categories</a>
    </div>
    <h2>Product Management</h2>
    <!-- Product Form -->
    <div class="form-container">
        <form action="product-servlet" method="post">
            <input type="hidden" name="actionKey" value="<%= hasProduct && formProduct.getId() > 0 ? "update" : "add" %>">
            <input type="hidden" name="id" value="<%= hasProduct ? formProduct.getId() : "" %>">

            <!-- Other form fields -->
            <div class="form-group">
                <label for="nom">Name:</label>
                <input type="text" id="nom" name="nom" value="<%= hasProduct ? formProduct.getNom() : "" %>" required>
            </div>

            <div class="form-group">
                <label for="description">Description:</label>
                <textarea id="description" name="description" rows="3" required><%= hasProduct ? formProduct.getDescription() : "" %></textarea>
            </div>

            <div class="form-group">
                <label for="prix">Price:</label>
                <input type="number" step="0.01" id="prix" name="prix" value="<%= hasProduct ? formProduct.getPrix() : "" %>" required>
            </div>

            <div class="form-group">
                <label for="quantite">Quantity:</label>
                <input type="number" id="quantite" name="quantite" value="<%= hasProduct ? formProduct.getQuantite() : "" %>" required>
            </div>

            <div class="form-group">
                <label for="categoryId">Category:</label>
                <select id="categoryId" name="categoryId" required>
                    <option value="">Select Category</option>
                    <%
                        for(Category category : categories) {
                            boolean isSelected = hasProduct && formProduct.getCategory() != null &&
                                    formProduct.getCategory().getId() == category.getId();
                    %>
                    <option value="<%=category.getId()%>" <%= isSelected ? "selected" : "" %>>
                        <%=category.getNom()%>
                    </option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label>
                    <input type="checkbox" name="selected" <%= hasProduct && formProduct.isSelected() ? "checked" : "" %>>
                    Active
                </label>
            </div>
            <button type="submit" class="btn btn-primary">
                <%= hasProduct && formProduct.getId() > 0 ? "Update" : "Add" %> Product
            </button>
            <% if (hasProduct && formProduct.getId() > 0) { %>
            <a href="product-servlet" class="btn btn-warning">Cancel</a>
            <% } %>
        </form>    </div>

    <!-- Products Table -->
    <div class="form-group">
        <label for="search">Search:</label>
        <form action="product-servlet" method="get">
            <input type="hidden" name="actionKey" value="filter">
            <input type="text" id="search" name="filter" class="form-control" placeholder="Search products">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Category</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            System.out.println("products: " + products);
            if(products != null) {
                for(Product product : products) {
        %>
        <tr>
            <td><%=product.getId()%></td>
            <td><%=product.getNom()%></td>
            <td><%=product.getDescription()%></td>
            <td><%=product.getPrix()%></td>
            <td><%=product.getQuantite()%></td>
            <td><%=product.getCategory().getNom()%></td>
            <td><%=product.isSelected() ? "Active" : "Inactive"%></td>
            <td class="action-buttons">
                <a href="product-servlet?actionKey=edit&id=<%=product.getId()%>"
                   class="btn btn-warning">Edit</a>
                <form action="product-servlet" method="post" style="display: inline;">
                    <input type="hidden" name="actionKey" value="delete">
                    <input type="hidden" name="id" value="<%=product.getId()%>">
                    <button type="submit" class="btn btn-danger"
                            onclick="return confirm('Are you sure you want to delete this product?')">
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