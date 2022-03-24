package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.CartDetail;
import com.hcmute.backendtechnologicalapplianceswebsite.model.OrderDetail;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.CartDetailRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class CartController {
    private final CartDetailRepository cartRepository;

    public CartController(CartDetailRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    @GetMapping("cartdetails")
    public ResponseEntity<CartDetail> getAllCoupons(@PathVariable String id)
    {
        CartDetail orderDetail=cartRepository.findByUsername(id);
        if (orderDetail == null)
            throw new ResourceNotFoundException("Username not found with id: " + id);
        return ResponseEntity.ok(orderDetail);
    }

    @PostMapping("/cartdetails")
    public CartDetail createOrderDetail(@RequestBody CartDetail cartDetail) {
        return cartRepository.save(cartDetail);
    }

    @GetMapping("/cartdetails/{username}/{productid}")
    public ResponseEntity<CartDetail> getOrderById(@PathVariable String username,String productid) {
        CartDetail cartdetail = cartRepository.findByUsernameAndProductId(username,productid);
        if (cartdetail == null)
            throw new ResourceNotFoundException("Cart not found with username: " + username +"productid"+productid);
        return ResponseEntity.ok(cartdetail);
    }

    //    Update brand
    @PutMapping("/orders/{username}/{productid}")
    public ResponseEntity<CartDetail> updateOrder(@PathVariable String username,@PathVariable String productid, @RequestBody CartDetail order) {
        CartDetail _cart = cartRepository.findByUsernameAndProductId(username,productid);
        if (_cart == null)
            throw new ResourceNotFoundException("Cart not found with username: " + username +"productid"+productid);

        _cart.setId(order.getId());
        _cart.setQuantity(order.getQuantity());

        cartRepository.save(_cart);
        return ResponseEntity.ok(_cart);
    }


    @DeleteMapping("/orders/{username}/{productid}")
    public ResponseEntity<CartDetail> deleteCoupon(@PathVariable String username,@PathVariable String productid) {
        CartDetail order = cartRepository.findByUsernameAndProductId(username,productid);
        if (order == null)
            throw new ResourceNotFoundException("Cart not found with username: " + username +"productid"+productid);
        cartRepository.delete(order);
        return ResponseEntity.ok(order);
    }


}
