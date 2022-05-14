package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.CartDetail;
import com.hcmute.backendtechnologicalapplianceswebsite.model.CartDetailId;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.CartDetailRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ProductRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
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

        log.info("Get all cart details of user: {}", username);
        if (cartDetails.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(cartDetails);
        }
    }

    @PostMapping("/cart-details")
    public CartDetail createCartDetail(@RequestBody CartDetail cartDetail) {
        User user = userRepository.findById(cartDetail.getId().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + cartDetail.getId().getUsername()));
        Product product = productRepository.findById(cartDetail.getId().getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartDetail.getId().getProductId()));

        if (cartDetailRepository.existsById(cartDetail.getId())) {
            CartDetail newCartDetail = cartDetailRepository.findById(cartDetail.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("CartDetail not found with id: " + cartDetail.getId()));
            newCartDetail.setQuantity(newCartDetail.getQuantity() + cartDetail.getQuantity());

            log.info("CartDetail is existed, update quantity of cart detail: {}", cartDetail.getId());
            return cartDetailRepository.save(newCartDetail);
        } else {
            cartDetail.setUser(user);
            cartDetail.setProduct(product);

            log.info("Create new cart detail: {}", cartDetail.getId());
            return cartDetailRepository.save(cartDetail);
        }
    }

    @GetMapping("/cart-details/{username}/{productId}")
    public ResponseEntity<CartDetail> getCartDetail(@PathVariable String username, @PathVariable String productId) {
        CartDetailId cartDetailId = new CartDetailId(username, productId);
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartDetailId));

        log.info("Get cart detail: {}", cartDetailId);
        return ResponseEntity.ok(cartDetail);
    }

    @PutMapping("/cart-details/{username}/{productId}")
    public CartDetail updateCartDetail(@RequestBody CartDetail data, @PathVariable String username, @PathVariable String productId) {
        CartDetailId cartDetailId = new CartDetailId(username, productId);
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartDetailId));
        cartDetail.setQuantity(data.getQuantity());

        log.info("Update cart detail: {}", cartDetailId);
        return cartDetailRepository.save(cartDetail);
    }


    @DeleteMapping("/cart-details/{username}/{productId}")
    public ResponseEntity<CartDetail> deleteCartDetail(@PathVariable String username, @PathVariable String productId) {
        CartDetailId cartDetailId = new CartDetailId(username, productId);
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartDetailId));
        cartDetailRepository.delete(cartDetail);

        log.info("Delete cart detail: {}", cartDetailId);
        return ResponseEntity.ok(cartDetail);
    }

}
