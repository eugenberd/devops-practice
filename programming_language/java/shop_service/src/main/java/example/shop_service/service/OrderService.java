package com.example.shop_service.service;

import com.example.shop_service.model.Order;
import com.example.shop_service.model.Product;
import com.example.shop_service.repository.OrderRepository;
import com.example.shop_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        Order order = new Order();
        order.setProducts(products);
        return orderRepository.save(order);
    }

    public Optional<Order> getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
