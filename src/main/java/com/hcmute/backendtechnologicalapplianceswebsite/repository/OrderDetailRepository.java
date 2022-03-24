package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Order;
import com.hcmute.backendtechnologicalapplianceswebsite.model.OrderDetail;
import com.hcmute.backendtechnologicalapplianceswebsite.model.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    OrderDetail findByOrderId(String id);

    OrderDetail findByOrderAndProductId(String orderid, String productid);
}