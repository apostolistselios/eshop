package com.eshop.backend.repositories;

import com.eshop.backend.models.Shop;
import com.eshop.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    boolean existsByTin(String tin);

    Optional<Shop> findByTin(String tin);

    Optional<Shop> findByUser(User user);
}
