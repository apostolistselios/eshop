package com.eshop.backend.repositories;

import com.eshop.backend.models.Customer;
import com.eshop.backend.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(Customer customer);
}
