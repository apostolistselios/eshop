package com.eshop.backend.repositories;

import com.eshop.backend.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    boolean existsByTin(String tin);

    Optional<Shop> findByTin(String tin);
}
