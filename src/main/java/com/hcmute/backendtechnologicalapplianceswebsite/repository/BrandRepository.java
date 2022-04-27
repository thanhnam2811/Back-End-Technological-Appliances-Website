package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Brand;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, String> {
    Brand findByBrandId(String id);

    default String generateBrandId() {

        List<Brand> brands = findAll();

        brands.sort((c1, c2) -> {
            Long id1 = Long.parseLong(c1.getBrandId().substring(1));
            Long id2 = Long.parseLong(c2.getBrandId().substring(1));
            return id2.compareTo(id1);
        });

        String lastBrandId = brands.get(0).getBrandId();

        return "B" + String.format("%02d", Long.parseLong(lastBrandId.substring(1)) + 1);
    }
}