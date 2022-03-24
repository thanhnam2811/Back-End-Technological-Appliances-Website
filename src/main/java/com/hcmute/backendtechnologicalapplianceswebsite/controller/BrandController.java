package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Brand;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.BrandRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class BrandController {
    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    // Get All Brands
    @GetMapping("/brands")
    public Iterable<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    //    Create brand
    @PostMapping("/brands")
    public Brand createBrand(@RequestBody Brand brand) {
        //  Default value for brandId
        brand.setBrandId(brandRepository.generateBrandId());
        return brandRepository.save(brand);
    }

    //    Get brand by id
    @GetMapping("/brands/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable String id) {
        Brand brand = brandRepository.findByBrandId(id);
        if (brand == null)
            throw new ResourceNotFoundException("Brand not found with id: " + id);
        return ResponseEntity.ok(brand);
    }

    //    Update brand
    @PutMapping("/brands/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable String id, @RequestBody Brand brand) {
        Brand _brand = brandRepository.findByBrandId(id);
        if (_brand == null)
            throw new ResourceNotFoundException("Brand not found with id: " + id);
        _brand.setName(brand.getName());
        _brand.setEmail(brand.getEmail());
        _brand.setLocation(brand.getLocation());
        _brand.setLogo(brand.getLogo());
        brandRepository.save(_brand);
        return ResponseEntity.ok(_brand);
    }

    //    Delete brand
    @DeleteMapping("/brands/{id}")
    public ResponseEntity<Brand> deleteBrand(@PathVariable String id) {
        Brand brand = brandRepository.findByBrandId(id);
        if (brand == null)
            throw new ResourceNotFoundException("Brand not found with id: " + id);
        brandRepository.delete(brand);
        return ResponseEntity.ok(brand);
    }
}
