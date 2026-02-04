package com.eshop.backend.repositories;

import com.eshop.backend.models.Customer;
import com.eshop.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByTin(String tin);

    Optional<Customer> findByUser(User user);
}
