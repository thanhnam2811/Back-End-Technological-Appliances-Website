package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Delivery;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Order;
import com.hcmute.backendtechnologicalapplianceswebsite.model.OrderDetailId;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Order findByOrderId(String id);
    default String generateOrderId() {
        String PREFIX = "O";

        List<Order> orders = findAll();

        orders.sort((c1, c2) -> {
            Long id1 = Long.parseLong(c1.getOrderId().substring(PREFIX.length()));
            Long id2 = Long.parseLong(c2.getOrderId().substring(PREFIX.length()));
            return id2.compareTo(id1);
        });

        String lastOrderId = orders.get(0).getOrderId();

        return PREFIX + String.format("%05d", Long.parseLong(lastOrderId.substring(2)) + 1);
    }

    Iterable<Order> findAllByUser(User user);
}