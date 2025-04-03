package com.Inventory.Management;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service // holds business logic
public class ProductService {

    @Autowired // inject ProductRepository dependency
    private ProductRepository productRepository;

    // Get all products (non-paginated)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get paginated products
    // Duplicate method removed

    // Search products with pagination
    public Page<Product> searchProducts(String query, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Save or update product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Delete product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Export to CSV
    public void exportToCSV(Writer writer) throws IOException {
        List<Product> products = productRepository.findAll();
        PrintWriter printWriter = new PrintWriter(writer);
        
        // Write CSV header
        printWriter.println("ID,Name,Quantity,Price");
        
        // Write product data
        products.forEach(product -> 
            printWriter.printf("%d,%s,%d,%.2f%n",
                product.getId(),
                escapeCsv(product.getName()),
                product.getQuantity(),
                product.getPrice())
        );
        
        printWriter.flush();
    }

    private String escapeCsv(String input) {
        if (input == null) return "";
        return input.contains(",") ? "\"" + input + "\"" : input;
    }
}