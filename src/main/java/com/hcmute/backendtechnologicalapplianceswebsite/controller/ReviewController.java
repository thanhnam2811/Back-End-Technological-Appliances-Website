package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Review;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ProductRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewController(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    // Get All Reviews
    @GetMapping("/reviews")
    public Iterable<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Get all reviews by product id
    @GetMapping("/reviews/product/{productId}")
    public Iterable<Review> getAllReviewsByProductId(@PathVariable(value = "productId") String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return reviewRepository.findAllByProduct(product);
    }

    //    Create review
    @PostMapping("/reviews")
    public Review createReview(@RequestBody Review review) {
        //  Default value for reviewId
        review.setReviewId(reviewRepository.generateReviewId());
        return reviewRepository.save(review);
    }

    //    Get review by id
    @GetMapping("/reviews/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Review review = reviewRepository.findByReviewId(id);
        if (review == null)
            throw new ResourceNotFoundException("Review not found with id: " + id);
        return ResponseEntity.ok(review);
    }

    //    Update review
    @PutMapping("/reviews/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable String id, @RequestBody Review review) {
        Review _review = reviewRepository.findByReviewId(id);
        if (_review == null)
            throw new ResourceNotFoundException("Review not found with id: " + id);
        _review.setContent(review.getContent());
        _review.setRate(review.getRate());
        _review.setTime(review.getTime());
        _review.setProduct(review.getProduct());
        _review.setUser(review.getUser());
        reviewRepository.save(_review);
        return ResponseEntity.ok(_review);
    }

    //    Delete review
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Review> deleteReview(@PathVariable String id) {
        Review review = reviewRepository.findByReviewId(id);
        if (review == null)
            throw new ResourceNotFoundException("Review not found with id: " + id);
        reviewRepository.delete(review);
        return ResponseEntity.ok(review);
    }
}
