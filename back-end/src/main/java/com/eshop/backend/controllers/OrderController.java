package com.eshop.backend.controllers;

import com.eshop.backend.dto.order.OrderResponseDto;
import com.eshop.backend.mappers.OrderMapper;
import com.eshop.backend.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Order")
@RestController
@RequestMapping(path = "order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderResponseDto> getOrders() {
        return this.orderService.getOrdersByCustomer()
                .stream()
                .map((order) -> OrderMapper.toDto(order))
                .toList();
    }
}
