package com.eshop.backend.repositories;

import com.eshop.backend.models.Cart;
import com.eshop.backend.models.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomer(Customer customer);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Cart> findWithItemsByCustomer(Customer customer);
}
