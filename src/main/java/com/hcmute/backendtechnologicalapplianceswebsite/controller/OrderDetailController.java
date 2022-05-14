package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.*;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.OrderDetailRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.OrderRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ProductRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@RestController
@RequestMapping("/api/technological_appliances/")
public class OrderDetailController {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderDetailController(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Get all order details by username
    @GetMapping("/order-details/username/{username}")
    public List<OrderDetail> getOrderDetailByUsername(@PathVariable String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        Collection<Order> orders = new ArrayList<>();
        orderRepository.findAllByUser(user).forEach(orders::add);

        log.info("Get all order details by username: " + username);
        return orderDetailRepository.findAllByOrderIn(orders);
    }

    @GetMapping("/order-details/{orderId}")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails(@PathVariable String orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllById_OrderId(orderId);

        log.info("Get all order details by orderId: " + orderId);
        return ResponseEntity.ok().body(orderDetails);
    }

    @PostMapping("/order-details")
    public OrderDetail createOrderDetail(@RequestBody OrderDetail orderDetail) {
        Order order = orderRepository.findById(orderDetail.getId().getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderDetail.getId().getOrderId()));
        Product product = productRepository.findById(orderDetail.getId().getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + orderDetail.getId().getProductId()));
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);

        log.info("Create order detail: " + orderDetail);
        return orderDetailRepository.save(orderDetail);
    }

    @GetMapping("/order-details/{orderId}/{productId}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable String orderId, @PathVariable String productId) {
        OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));

        log.info("Get order detail by id: " + orderDetailId);
        return ResponseEntity.ok(orderDetail);
    }

    // Update orderDetail
    @PutMapping("/order-details/{orderId}/{productId}")
    public ResponseEntity<OrderDetail> updateOrderDetail(@PathVariable String orderId, @PathVariable String productId, @RequestBody OrderDetail order) {
        OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));
        orderDetail.setQuantity(order.getQuantity());
        orderDetail.setPrice(order.getPrice());
        orderDetail.setTotalPrice(order.getTotalPrice());

        OrderDetail updatedOrderDetail = orderDetailRepository.save(orderDetail);

        log.info("Update order detail: " + orderDetail);
        return ResponseEntity.ok(updatedOrderDetail);
    }


    @DeleteMapping("/orders/{orderId}/{productId}")
    public ResponseEntity<OrderDetail> deleteOrderDetail(@PathVariable String orderId, @PathVariable String productId) {
        OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));
        orderDetailRepository.delete(orderDetail);

        log.info("Delete order detail: " + orderDetail);
        return ResponseEntity.ok().build();
    }

}
