<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<html>
<head>
    <title>Product Inventory</title>
    <link rel="stylesheet" href="/css/style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
   
</head>
<body>
    <div class="container">
        <h1>Inventory Management System</h1>
        
        <!-- Action Buttons -->
        <div class="action-bar">
            <a href="/products/new" class="btn btn-primary">Add New Product</a>
            <a href="/products/export/csv" class="btn btn-secondary">Export to CSV</a>
        </div>

        <!-- Search Form -->
        <div class="search-container">
            <form action="/products/search" method="get" class="search-form">
                <input type="text" name="query" placeholder="Search by product name..." 
                       value="${param.query}" class="search-input">
                <button type="submit" class="btn btn-search">Search</button>
                <a href="/products" class="btn btn-clear">Clear</a>
            </form>
        </div>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

        <!-- Set the locale to India -->
        <fmt:setLocale value="en_IN" />
        <fmt:setBundle basename="messages" />
        
        <!-- Format the price in Indian Rupees -->
        <td><fmt:formatNumber value="${product.price}" type="currency" /></td>
        <!-- Status Messages -->
        <c:if test="${not empty success}">
            <div class="alert alert-success">${fn:escapeXml(success)}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${fn:escapeXml(error)}</div>
        </c:if>

        <!-- Product Table -->
        <table class="product-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty products}">
                        <tr>
                            <td colspan="5" class="no-results">No products found</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${products}" var="product">
                            <tr class="${product.quantity < 5 ? 'critical-stock' : (product.quantity < 10 ? 'low-stock' : '')}">
                                <td>${product.id}</td>
                                <td>${fn:escapeXml(product.name)}</td>
                                <td>${product.quantity}</td>
                                <td><fmt:formatNumber value="${product.price}" type="currency"/></td>
                                <td class="actions">
                                    <a href="/products/edit/${product.id}" class="btn btn-edit">Edit</a>
                                    <a href="/products/delete/${product.id}" class="btn btn-delete" 
                                       onclick="return confirm('Are you sure you want to delete ${fn:escapeXml(product.name)}?')">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:forEach begin="0" end="${totalPages - 1}" var="i">
                    <a href="/products?page=${i}&query=${fn:escapeXml(param.query)}" 
                       class="${page == i ? 'active' : ''}">${i + 1}</a>
                </c:forEach>
            </div>
        </c:if>
    </div>
</body>
</html>