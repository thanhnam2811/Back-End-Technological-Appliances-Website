package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.model.*;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.OrderDetailRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.OrderRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ProductRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username));
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

    @PostMapping("/order-details/{username}")
    public ResponseEntity<OrderDetail> createOrderDetail(@RequestBody OrderDetail orderDetail, @PathVariable String username) {
        Order order = orderRepository.findById(orderDetail.getId().getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + orderDetail.getId().getOrderId()));
        if (order.getUser().getUsername().equals(username)) {
            Product product = productRepository.findById(orderDetail.getId().getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + orderDetail.getId().getProductId()));
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);

            log.info("Create order detail: " + orderDetail);
            return ResponseEntity.ok().body(orderDetailRepository.save(orderDetail));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't create order detail for this order");
        }
    }

    @GetMapping("/order-details/{orderId}/{productId}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable String orderId, @PathVariable String productId) {
        OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetail not found with id: " + orderDetailId));

        log.info("Get order detail by id: " + orderDetailId);
        return ResponseEntity.ok(orderDetail);
    }

    // Update orderDetail
    @PutMapping("/order-details/{username}/{orderId}/{productId}")
    public ResponseEntity<OrderDetail> updateOrderDetail(@PathVariable String orderId, @PathVariable String productId, @RequestBody OrderDetail detail, @PathVariable String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + orderId));
        if (order.getUser().getUsername().equals(username)) {

            OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetail not found with id: " + orderDetailId));
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setPrice(detail.getPrice());
            orderDetail.setTotalPrice(detail.getTotalPrice());

            OrderDetail updatedOrderDetail = orderDetailRepository.save(orderDetail);

            log.info("Update order detail: " + orderDetail);
            return ResponseEntity.ok(updatedOrderDetail);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't update order detail for this order");
        }
    }


    @DeleteMapping("/orders/{orderId}/{productId}")
    public ResponseEntity<OrderDetail> deleteOrderDetail(@PathVariable String orderId, @PathVariable String productId) {
        OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetail not found with id: " + orderDetailId));
        orderDetailRepository.delete(orderDetail);

        log.info("Delete order detail: " + orderDetail);
        return ResponseEntity.ok().build();
    }

}
