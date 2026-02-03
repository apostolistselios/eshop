package com.eshop.backend.repositories;

import com.eshop.backend.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerId(Long customerId);

    boolean existsByCustomerId(Long customerId);

//    @EntityGraph(attributePaths = {"items", "items.product"})
//    Optional<Cart> findWithItemsByCustomerId(Long customerId);
}
