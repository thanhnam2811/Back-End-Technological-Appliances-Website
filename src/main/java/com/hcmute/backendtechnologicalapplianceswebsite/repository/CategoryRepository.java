package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByCategoryId(String id);

    default String generateCategoryId() {
        List<Category> categories = findAll();

        categories.sort((c1, c2) -> {
            Long id1 = Long.parseLong(c1.getCategoryId().substring(1));
            Long id2 = Long.parseLong(c2.getCategoryId().substring(1));
            return id2.compareTo(id1);
        });

        String lastCategoryId = categories.get(0).getCategoryId();

        return "C" + String.format("%02d", Long.parseLong(lastCategoryId.substring(1)) + 1);
    }
}