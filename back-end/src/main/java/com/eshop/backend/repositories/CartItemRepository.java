package com.eshop.backend.repositories;

import com.eshop.backend.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
//    List<CartItem> findAllByCartId(Long cartId);
//
//    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
//
//    void deleteByCartIdAndProductId(Long cartId, Long productId);
//
//    long deleteByCartId(Long cartId);
}
