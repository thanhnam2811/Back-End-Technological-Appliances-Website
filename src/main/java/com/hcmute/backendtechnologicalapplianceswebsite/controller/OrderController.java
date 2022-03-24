package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Coupon;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Order;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @GetMapping("orders")
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestBody Order order) {

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
        _order.setPhoneNumer(order.getPhoneNumer());
        _order.setUsername(order.getUsername());
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
