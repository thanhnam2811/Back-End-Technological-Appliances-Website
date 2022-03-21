package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
}