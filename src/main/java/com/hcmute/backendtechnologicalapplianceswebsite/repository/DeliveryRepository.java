package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Delivery;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    Delivery findByDeliveryId(String id);

    default String generateDeliveryId() {

        List<Delivery> deliveries = findAll();

        deliveries.sort((c1, c2) -> c2.getDeliveryId().compareTo(c1.getDeliveryId()));

        String lastDeleveryId = deliveries.get(0).getDeliveryId();

        return "DL" + String.format("%05d", Integer.parseInt(lastDeleveryId.substring(2)) + 1);
    }
}