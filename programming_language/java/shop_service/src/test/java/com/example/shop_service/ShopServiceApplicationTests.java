package com.example.shop_service;

import com.example.shop_service.model.Order;
import com.example.shop_service.model.Product;
import com.example.shop_service.repository.OrderRepository;
import com.example.shop_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShopServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        orderRepository.deleteAll();

        Product product1 = new Product();
        product1.setName("Laptop");
        product1.setPrice(1500);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Smartphone");
        product2.setPrice(700);
        productRepository.save(product2);
    }

    @Test
    void testCreateOrder() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2]"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"products\":[{\"id\":1,\"name\":\"Laptop\",\"price\":1500.0},{\"id\":2,\"name\":\"Smartphone\",\"price\":700.0}]}"));
    }

    @Test
    void testGetOrder() throws Exception {
        Order order = new Order();
        order.setProducts(Arrays.asList(productRepository.findById(1L).get(), productRepository.findById(2L).get()));
        orderRepository.save(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"products\":[{\"id\":1,\"name\":\"Laptop\",\"price\":1500.0},{\"id\":2,\"name\":\"Smartphone\",\"price\":700.0}]}"));
    }

    @Test
    void testGetOrderNotFound() throws Exception {
        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound());
    }
}
