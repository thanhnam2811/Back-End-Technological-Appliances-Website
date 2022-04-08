package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.CartDetail;
import com.hcmute.backendtechnologicalapplianceswebsite.model.CartDetailId;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.CartDetailRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ProductRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class CartDetailController {
    private final CartDetailRepository cartDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartDetailController(CartDetailRepository cartDetailRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartDetailRepository = cartDetailRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    @GetMapping("/cart-details/{username}")
    public ResponseEntity<List<CartDetail>> getCartDetails(@PathVariable String username) {
        List<CartDetail> cartDetails = cartDetailRepository.findAllById_Username(username);
        return ResponseEntity.ok(cartDetails);
    }

    @PostMapping("/cart-details")
    public CartDetail createCartDetail(@RequestBody CartDetail cartDetail) {
        User user = userRepository.findById(cartDetail.getId().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + cartDetail.getId().getUsername()));
        Product product = productRepository.findById(cartDetail.getId().getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartDetail.getId().getProductId()));

        if (cartDetailRepository.existsById(cartDetail.getId())) {
            CartDetail newCartDetail = cartDetailRepository.findById(cartDetail.getId()).get();
            newCartDetail.setQuantity(newCartDetail.getQuantity() + cartDetail.getQuantity());
            return cartDetailRepository.save(newCartDetail);
        } else {
            cartDetail.setUser(user);
            cartDetail.setProduct(product);
            return cartDetailRepository.save(cartDetail);
        }
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
