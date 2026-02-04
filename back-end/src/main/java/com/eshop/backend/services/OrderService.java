package com.eshop.backend.services;

import com.eshop.backend.models.*;
import com.eshop.backend.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrdersByCustomer() {
        Customer customer = this.customerService.findByCurrentUser();
        return this.orderRepository.findAllByCustomer(customer);
    }

    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            Product p = cartItem.getProduct();
            Long qty = cartItem.getQuantity();
            BigDecimal unitPrice = cartItem.getUnitPrice() != null ? cartItem.getUnitPrice() : p.getPrice();

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(p);
            orderItem.setQuantity(qty);
            orderItem.setUnitPrice(unitPrice);

            order.addItem(orderItem);

            total = total.add(unitPrice.multiply(BigDecimal.valueOf(qty)));
        }

        return this.orderRepository.save(order);
    }
}
