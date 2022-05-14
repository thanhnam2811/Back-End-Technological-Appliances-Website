package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Review;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ProductRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ReviewRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@RestController
@RequestMapping("/api/technological_appliances/")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewController(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Get All Reviews
    @GetMapping("/reviews")
    public Iterable<Review> getAllReviews() {
        log.info("Get all reviews");
        return reviewRepository.findAll();
    }

    // Get all reviews by product id
    @GetMapping("/reviews/product/{productId}")
    public Iterable<Review> getAllReviewsByProductId(@PathVariable(value = "productId") String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        log.info("Get all reviews by product id: " + productId);
        return reviewRepository.findAllByProduct(product);
    }

    //    Create review
    @PostMapping("/reviews/product/{productId}/user/{username}")
    public Review createReview(@RequestBody Review review, @PathVariable(value = "productId") String productId, @PathVariable(value = "username") String username) {
        //  Default value for reviewId
        review.setReviewId(reviewRepository.generateReviewId());

        // Product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        review.setProduct(product);

        // User
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        review.setUser(user);

        // Time
        if (review.getTime() == null) {
            review.setTime(new Date());
        }

        log.info("Create review: " + review);
        return reviewRepository.save(review);
    }

    //    Get review by id
    @GetMapping("/reviews/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        log.info("Get review by id: " + id);
        return ResponseEntity.ok(review);
    }

    //    Update review
    @PutMapping("/reviews/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable String id, @RequestBody Review review) {
        Review _review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        _review.setContent(review.getContent());
        _review.setRate(review.getRate());

        // Time
        if (review.getTime() == null)
            _review.setTime(new Date());
        else
            _review.setTime(review.getTime());

        reviewRepository.save(_review);

        log.info("Update review: " + _review);
        return ResponseEntity.ok(_review);
    }

    //    Delete review
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Review> deleteReview(@PathVariable String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        reviewRepository.delete(review);

        log.info("Delete review: " + review);
        return ResponseEntity.ok(review);
    }
}
