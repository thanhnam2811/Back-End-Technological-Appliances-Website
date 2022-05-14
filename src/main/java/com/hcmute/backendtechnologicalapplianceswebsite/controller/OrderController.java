package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Delivery;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Order;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.DeliveryRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.OrderRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@RestController
@RequestMapping("/api/technological_appliances/")
public class OrderController {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;

    public OrderController(OrderRepository orderRepository, UserRepository userRepository, DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
    }


    @GetMapping("/orders")
    public Iterable<Order> getAllOrders() {
        log.info("Get all orders");
        return orderRepository.findAll();
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestBody Order order) {
        // User
        User user = userRepository.findById(order.getUser().getUsername())
                .orElseThrow((() -> new ResourceNotFoundException("User not found with username: " + order.getUser().getUsername())));
        order.setUser(user);

        // Delivery
        Delivery delivery = deliveryRepository.findById(order.getDelivery().getDeliveryId())
                .orElseThrow((() -> new ResourceNotFoundException("Delivery not found with deliveryId: " + order.getDelivery().getDeliveryId())));
        order.setDelivery(delivery);

        // Id
        order.setOrderId(orderRepository.generateOrderId());

        log.info("Create order: {}", order);
        return orderRepository.save(order);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("Order not found with id: " + id)));

        log.info("Get order by id: {}", id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders/username/{username}")
    public Iterable<Order> getOrderByUsername(@PathVariable String username) {
        User user = userRepository.findById(username)
                .orElseThrow((() -> new ResourceNotFoundException("User not found with username: " + username)));

        log.info("Get order by username: {}", username);
        return orderRepository.findAllByUser(user);
    }

    //    Update brand
    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable String id, @RequestBody Order order) {
        Order _order = orderRepository.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("Order not found with id: " + id)));
        order.setOrderId(_order.getOrderId());

        log.info("Update order: {}", order);
        return ResponseEntity.ok(orderRepository.save(order));
    }


    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Order> deleteCoupon(@PathVariable String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("Order not found with id: " + id)));
        orderRepository.delete(order);

        log.info("Delete order: {}", order);
        return ResponseEntity.ok(order);
    }
}
