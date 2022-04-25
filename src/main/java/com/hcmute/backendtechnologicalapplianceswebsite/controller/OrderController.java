package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Delivery;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Order;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.DeliveryRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.OrderRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
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
        return orderRepository.findAll();
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestBody Order order) {
        // User
        User user = userRepository.findByUsername(order.getUser().getUsername());
        if (user == null)
            throw new ResourceNotFoundException("User not found with username: " + order.getUser().getUsername());
        order.setUser(user);

        // Delivery
        Delivery delivery = deliveryRepository.findByDeliveryId(order.getDelivery().getDeliveryId());
        if (delivery == null)
            throw new ResourceNotFoundException("Delivery not found with id: " + order.getDelivery().getDeliveryId());
        order.setDelivery(delivery);

        // Id
        order.setOrderId(orderRepository.generateOrderId());
        return orderRepository.save(order);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        Order order = orderRepository.findByOrderId(id);
        if (order == null)
            throw new ResourceNotFoundException("Order not found with id: " + id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders/username/{username}")
    public Iterable<Order> getOrderByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new ResourceNotFoundException("User not found with username: " + username);
        return orderRepository.findAllByUser(user);
    }

    //    Update brand
    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable String id, @RequestBody Order order) {
        Order _order = orderRepository.findByOrderId(id);
        if (_order == null)
            throw new ResourceNotFoundException("Order not found with id: " + id);

        _order.setName(order.getName());
        _order.setAddress(order.getAddress());
        _order.setCouponId(order.getCouponId());
        _order.setDelivery(order.getDelivery());
        _order.setDiscountPrice(order.getDiscountPrice());
        _order.setPurchaseDate(order.getPurchaseDate());
        _order.setPhoneNumber(order.getPhoneNumber());
        _order.setUser(order.getUser());
        _order.setStatus(order.getStatus());
        _order.setTotalPrices(order.getTotalPrices());

        orderRepository.save(_order);
        return ResponseEntity.ok(_order);
    }


    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Order> deleteCoupon(@PathVariable String id) {
        Order order = orderRepository.findByOrderId(id);
        if (order == null)
            throw new ResourceNotFoundException("Order not found with id: " + id);
        orderRepository.delete(order);
        return ResponseEntity.ok(order);
    }
}
