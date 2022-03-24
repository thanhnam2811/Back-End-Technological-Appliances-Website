package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Brand;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Coupon;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.BrandRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.CouponRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class CouponController {
    private final CouponRepository couponRepository;

    public CouponController(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @GetMapping("coupons")
    public Iterable<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @PostMapping("/coupons")
    public Coupon createBrand(@RequestBody Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @GetMapping("/coupons/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable String id) {
        Coupon coupon = couponRepository.findByCouponId(id);
        if (coupon == null)
            throw new ResourceNotFoundException("Coupon not found with id: " + id);
        return ResponseEntity.ok(coupon);
    }

    //    Update brand
    @PutMapping("/coupons/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable String id, @RequestBody Coupon coupon) {
        Coupon _coupon = couponRepository.findByCouponId(id);
        if (_coupon == null)
            throw new ResourceNotFoundException("Coupon not found with id: " + id);
        _coupon.setDescription(coupon.getDescription());
        _coupon.setDiscount(coupon.getDiscount());
        _coupon.setEffectiveTime(coupon.getEffectiveTime());
        _coupon.setExpiredTime(coupon.getExpiredTime());
        couponRepository.save(_coupon);
        return ResponseEntity.ok(_coupon);
    }


    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<Coupon> deleteCoupon(@PathVariable String id) {
        Coupon coupon = couponRepository.findByCouponId(id);
        if (coupon == null)
            throw new ResourceNotFoundException("Coupon not found with id: " + id);
        couponRepository.delete(coupon);
        return ResponseEntity.ok(coupon);
    }
}
