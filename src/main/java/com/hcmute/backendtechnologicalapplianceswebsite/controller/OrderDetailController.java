package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.OrderDetail;
import com.hcmute.backendtechnologicalapplianceswebsite.model.OrderDetailId;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.OrderDetailRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class OrderDetailController {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailController(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }


    @GetMapping("/order-details/{orderId}")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails(@PathVariable String orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllById_OrderId(orderId);
        return ResponseEntity.ok().body(orderDetails);
    }

    @PostMapping("/order-details")
    public OrderDetail createOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @GetMapping("/order-details/{orderId}/{productId}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable String orderId, @PathVariable String productId) {
        OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));
        return ResponseEntity.ok(orderDetail);
    }

    //    Update brand
    @PutMapping("/order-details/{orderId}/{productId}")
    public ResponseEntity<OrderDetail> updateOrderDetail(@PathVariable String orderId, @PathVariable String productId, @RequestBody OrderDetail order) {
        OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));
        orderDetail.setQuantity(order.getQuantity());
        orderDetail.setPrice(order.getPrice());
        orderDetail.setTotalPrice(order.getTotalPrice());

        OrderDetail updatedOrderDetail = orderDetailRepository.save(orderDetail);
        return ResponseEntity.ok(updatedOrderDetail);
    }


    @DeleteMapping("/orders/{orderId}/{productId}")
    public ResponseEntity<OrderDetail> deleteOrderDetail(@PathVariable String orderId, @PathVariable String productId) {
        OrderDetailId orderDetailId = new OrderDetailId(orderId, productId);
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));
        orderDetailRepository.delete(orderDetail);
        return ResponseEntity.ok().build();
    }

}
