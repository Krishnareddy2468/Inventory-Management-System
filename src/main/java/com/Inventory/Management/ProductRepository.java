package com.Inventory.Management;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
//spring data jpa repository for CRUD operations
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> { //manages product entity

    // Basic search by name (case-insensitive)
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Find low stock products (quantity < threshold)
    List<Product> findByQuantityLessThan(int threshold);

    // Custom query for advanced search
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "CAST(p.price AS string) LIKE CONCAT('%', :query, '%') OR " +
           "CAST(p.quantity AS string) LIKE CONCAT('%', :query, '%')")
    Page<Product> advancedSearch(String query, Pageable pageable);

    // Find products sorted by price (asc/desc)
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);

    // Find products in price range
    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    // Count products by quantity threshold
    long countByQuantityLessThan(int threshold);
}