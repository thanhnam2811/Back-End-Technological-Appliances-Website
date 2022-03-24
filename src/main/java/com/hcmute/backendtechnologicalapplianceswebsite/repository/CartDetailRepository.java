package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.CartDetail;
import com.hcmute.backendtechnologicalapplianceswebsite.model.CartDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailRepository extends JpaRepository<CartDetail, CartDetailId> {
    CartDetail findByUsername(String id);

    CartDetail findByUsernameAndProductId(String username,String productid);
}