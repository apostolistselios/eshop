package com.eshop.backend.repositories;

import com.eshop.backend.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    long deleteByCartId(Long cartId);
}
