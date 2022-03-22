package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByCategoryId(String id);

    default String generateCategoryId() {
        List<Category> categories = findAll();

        categories.sort((c1, c2) -> c2.getCategoryId().compareTo(c1.getCategoryId()));

        String lastCategoryId = categories.get(0).getCategoryId();

        return "C" + String.format("%02d", Integer.parseInt(lastCategoryId.substring(1)) + 1);
    }
}