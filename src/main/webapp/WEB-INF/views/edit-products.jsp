<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Edit Product</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Edit Product</h1>
        <form action="/products/update/${product.id}" method="post">
            <input type="hidden" name="id" value="${product.id}">
            
            <div class="form-group">
                <label for="edit-name">Name:</label>
                <input type="text" id="edit-name" name="name" 
                       value="${product.name}" required class="form-control">
            </div>
            
            <div class="form-group">
                <label for="edit-quantity">Quantity:</label>
                <input type="number" id="edit-quantity" name="quantity" 
                       value="${product.quantity}" required min="0" class="form-control">
            </div>
            
            <div class="form-group">
                <label for="edit-price">Price:</label>
                <input type="number" id="edit-price" name="price" step="0.01"
                       value="${product.price}" required min="0" class="form-control">
            </div>
            
            <button type="submit" class="btn btn-primary">Update</button>
            <a href="/products" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
</body>
</html>