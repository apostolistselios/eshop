package com.eshop.backend.repositories;

import com.eshop.backend.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("""
            SELECT p FROM Product p
            WHERE (:type IS NULL OR LOWER(p.type) LIKE LOWER(CONCAT('%', :type, '%')))
              AND (:brand IS NULL OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%')))
              AND (:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
              AND (:minQuantity IS NULL OR p.quantity >= :minQuantity)
              AND (:maxQuantity IS NULL OR p.quantity <= :maxQuantity)
            """)
    Page<Product> findByFilters(
            @Param("type") String type,
            @Param("brand") String brand,
            @Param("description") String description,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minQuantity") Integer minQuantity,
            @Param("maxQuantity") Integer maxQuantity,
            Pageable pageable);

}
