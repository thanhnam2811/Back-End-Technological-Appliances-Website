package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Delivery;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {

    default String generateDeliveryId() {

        List<Delivery> deliveries = findAll();

        deliveries.sort((c1, c2) -> {
            Long id1 = Long.parseLong(c1.getDeliveryId().substring(1));
            Long id2 = Long.parseLong(c2.getDeliveryId().substring(1));
            return id2.compareTo(id1);
        });

        String lastDeleveryId = deliveries.get(0).getDeliveryId();

        return "DL" + String.format("%05d", Long.parseLong(lastDeleveryId.substring(2)) + 1);
    }
}