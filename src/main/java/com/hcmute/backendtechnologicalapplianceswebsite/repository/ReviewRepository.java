package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    default String generateReviewId() {
        List<Review> reviews = findAll();

        reviews.sort((c1, c2) -> {
            Long id1 = Long.parseLong(c1.getReviewId().substring(1));
            Long id2 = Long.parseLong(c2.getReviewId().substring(1));
            return id2.compareTo(id1);
        });

        String lastReviewId = reviews.get(0).getReviewId();

        System.out.println(lastReviewId);

        return "R" + String.format("%015d", Long.parseLong(lastReviewId.substring(1)) + 1);
    }

    Review findByReviewId(String id);

    Iterable<Review> findAllByProduct(Product product);
}