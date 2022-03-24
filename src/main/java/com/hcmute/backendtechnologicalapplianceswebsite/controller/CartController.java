package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.CartDetail;
import com.hcmute.backendtechnologicalapplianceswebsite.model.CartDetailId;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.CartDetailRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class CartController {
    private final CartDetailRepository cartDetailRepository;

    public CartController(CartDetailRepository cartDetailRepository) {
        this.cartDetailRepository = cartDetailRepository;
    }


    @GetMapping("/cart-details/{username}")
    public ResponseEntity<List<CartDetail>> getCartDetails(@PathVariable String username) {
        List<CartDetail> cartDetails = cartDetailRepository.findAllById_Username(username);
        return ResponseEntity.ok(cartDetails);
    }

    @PostMapping("/cart-details")
    public CartDetail createCartDetail(@RequestBody CartDetail cartDetail) {
        return cartDetailRepository.save(cartDetail);
    }

    @GetMapping("/cart-details/{username}/{productId}")
    public ResponseEntity<CartDetail> getCartDetail(@PathVariable String username, @PathVariable String productId) {
        CartDetailId cartDetailId = new CartDetailId(username, productId);
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartDetailId));
        return ResponseEntity.ok(cartDetail);
    }

    //    Update brand
    @PutMapping("/carts/{username}/{productId}")
    public ResponseEntity<CartDetail> updateCartDetail(@PathVariable String username, @PathVariable String productId, @RequestBody CartDetail cart) {
        CartDetailId cartDetailId = new CartDetailId(username, productId);
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartDetailId));

        cartDetail.setQuantity(cart.getQuantity());
        cartDetailRepository.save(cartDetail);
        return ResponseEntity.ok(cartDetail);
    }


    @DeleteMapping("/carts/{username}/{productId}")
    public ResponseEntity<CartDetail> deleteCartDetail(@PathVariable String username, @PathVariable String productId) {
        CartDetailId cartDetailId = new CartDetailId(username, productId);
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartDetailId));
        cartDetailRepository.delete(cartDetail);
        return ResponseEntity.ok(cartDetail);
    }


}
