package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Order;
import com.hcmute.backendtechnologicalapplianceswebsite.model.OrderDetail;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.OrderDetailRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class OrderDetailController {
    private final OrderDetailRepository orderdetailRepository;

    public OrderDetailController(OrderDetailRepository orderdetailRepository) {
        this.orderdetailRepository = orderdetailRepository;
    }


    @GetMapping("orderdetails")
    public ResponseEntity<OrderDetail> getAllCoupons(@PathVariable String id)
    {
        OrderDetail orderDetail=orderdetailRepository.findByOrderId(id);
        if (orderDetail == null)
            throw new ResourceNotFoundException("Order not found with id: " + id);
        return ResponseEntity.ok(orderDetail);
    }

    @PostMapping("/orderdetails")
    public OrderDetail createOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderdetailRepository.save(orderDetail);
    }

    @GetMapping("/orderdetails/{orderid}/{productid}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable String orderid,@PathVariable String productid) {
        OrderDetail orderdetail = orderdetailRepository.findByOrderAndProductId(orderid,productid);
        if (orderdetail == null)
            throw new ResourceNotFoundException("OrderDetail not found with orderid: " + orderid +"productid"+productid);
        return ResponseEntity.ok(orderdetail);
    }

    //    Update brand
    @PutMapping("/orderdetails/{orderid}/{productid}")
    public ResponseEntity<OrderDetail> updateOrder(@PathVariable String orderid,@PathVariable String productid, @RequestBody OrderDetail order) {
        OrderDetail _orderdetail = orderdetailRepository.findByOrderAndProductId(orderid,productid);
        if (_orderdetail == null)
            throw new ResourceNotFoundException("OrderDetail not found with orderid: " + orderid +"productid"+productid);

        _orderdetail.setId(order.getId());
        _orderdetail.setPrice(order.getPrice());
        _orderdetail.setQuantity(order.getQuantity());
        _orderdetail.setTotalPrice(order.getTotalPrice());

        orderdetailRepository.save(_orderdetail);
        return ResponseEntity.ok(_orderdetail);
    }


    @DeleteMapping("/orders/{orderid}/{productid}")
    public ResponseEntity<OrderDetail> deleteCoupon(@PathVariable String orderid,@PathVariable String productid) {
        OrderDetail orderdetail = orderdetailRepository.findByOrderAndProductId(orderid,productid);
        if (orderdetail == null)
            throw new ResourceNotFoundException("OrderDetail not found with orderid: " + orderid +"productid"+productid);
        orderdetailRepository.delete(orderdetail);
        return ResponseEntity.ok(orderdetail);
    }

}
