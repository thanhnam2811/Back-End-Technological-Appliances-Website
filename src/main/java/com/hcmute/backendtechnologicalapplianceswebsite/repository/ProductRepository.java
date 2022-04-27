package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Brand;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByProductId(String id);

    default String generateProductId() {

        List<Product> products = findAll();

        products.sort((c1, c2) -> {
            Long id1 = Long.parseLong(c1.getProductId().substring(1));
            Long id2 = Long.parseLong(c2.getProductId().substring(1));
            return id2.compareTo(id1);
        });

        String lastProductId = products.get(0).getProductId();

        return "P" + String.format("%05d", Long.parseLong(lastProductId.substring(1)) + 1);
    }

    Iterable<Product> findAllByBrand(Brand brand);
}