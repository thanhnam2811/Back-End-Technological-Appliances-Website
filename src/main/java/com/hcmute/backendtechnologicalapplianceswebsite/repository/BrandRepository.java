package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Brand;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, String> {
    Brand findByBrandId(String id);

    default String generateBrandId() {

        List<Brand> brands = findAll();

        brands.sort((c1, c2) -> c2.getBrandId().compareTo(c1.getBrandId()));

        String lastBrandId = brands.get(0).getBrandId();

        return "B" + String.format("%02d", Integer.parseInt(lastBrandId.substring(1)) + 1);
    }
}