package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}