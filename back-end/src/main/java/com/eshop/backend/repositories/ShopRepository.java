package com.eshop.backend.repositories;

import com.eshop.backend.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    boolean existsByTin(String tin);
}
