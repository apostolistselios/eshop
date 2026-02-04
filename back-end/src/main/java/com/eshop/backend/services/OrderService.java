package com.eshop.backend.services;

import com.eshop.backend.models.*;
import com.eshop.backend.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrder createOrder(Cart cart) {
        PurchaseOrder order = new PurchaseOrder();
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

        return this.purchaseOrderRepository.save(order);
    }
}
