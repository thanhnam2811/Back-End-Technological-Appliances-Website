package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    default String generateReviewId() {
        List<Review> reviews = findAll();

        reviews.sort((c1, c2) -> c2.getReviewId().compareTo(c1.getReviewId()));

        String lastReviewId = reviews.get(0).getReviewId();

        return "R" + String.format("%15d", Integer.parseInt(lastReviewId.substring(1)) + 1);
    }

    Review findByReviewId(String id);

    Iterable<Review> findAllByProduct(Product product);
}