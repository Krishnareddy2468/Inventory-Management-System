package com.Inventory.Management;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@Controller //handle web requests
@RequestMapping("/products") //end point
public class InventoryController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    
    @Autowired //inject ProductService dependency
    private ProductService productService;

    // List all products with pagination
    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String query,
            Model model) { //pass the model to the view
        
        Page<Product> productPage;
        if (query != null && !query.isEmpty()) {
            productPage = productService.searchProducts(query, PageRequest.of(page, DEFAULT_PAGE_SIZE));
        } else {
            productPage = productService.getAllProducts(PageRequest.of(page, DEFAULT_PAGE_SIZE));
        }
        
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("query", query);
        
        return "list-products";
    }

    @GetMapping("/search")
public String searchProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String query,
        Model model) {
    
    Page<Product> productPage;
    if (query != null && !query.isEmpty()) {
        productPage = productService.searchProducts(query, PageRequest.of(page, DEFAULT_PAGE_SIZE));
    } else {
        productPage = productService.getAllProducts(PageRequest.of(page, DEFAULT_PAGE_SIZE));
    }
    
    model.addAttribute("products", productPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());
    model.addAttribute("query", query);
    
    return "list-products";
}

    // Show ADD form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-products";
    }

    // Handle ADD form submission
    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            RedirectAttributes redirectAttrs) {
        
        if (result.hasErrors()) {
            return "add-products";
        }
        
        productService.saveProduct(product);
        redirectAttrs.addFlashAttribute("success", "Product added successfully!");
        return "redirect:/products";
    }

    // Show EDIT form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
        model.addAttribute("product", product);
        return "edit-products";
    }

    // Handle EDIT form submission
    @PostMapping("/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            RedirectAttributes redirectAttrs) {
        
        if (result.hasErrors()) {
            return "edit-products";
        }
        
        product.setId(id);
        productService.saveProduct(product);
        redirectAttrs.addFlashAttribute("success", "Product updated successfully!");
        return "redirect:/products";
    }

    // DELETE product
    @GetMapping("/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        
        productService.deleteProduct(id);
        redirectAttrs.addFlashAttribute("success", "Product deleted successfully!");
        return "redirect:/products";
    }

    // Export to CSV
    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=products.csv");
        productService.exportToCSV(response.getWriter());
    }
}