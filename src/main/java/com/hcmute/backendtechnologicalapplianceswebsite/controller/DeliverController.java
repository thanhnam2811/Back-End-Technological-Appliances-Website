package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Brand;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Delivery;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.DeliveryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class DeliverController {
    private final DeliveryRepository deliveryRepository;

    public DeliverController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }
    // Get All Categories
    @GetMapping("/deliveries")
    public Iterable<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @GetMapping("/deliveries/{id}")
    public ResponseEntity<Delivery> getProductById(@PathVariable String id) {
        Delivery delivery = deliveryRepository.findByDeliveryId(id);
        if (delivery == null)
            throw new ResourceNotFoundException("Delivery not found with id: " + id);
        return ResponseEntity.ok(delivery);
    }
    @PostMapping("/deliveries")
    public Delivery createDelivery(@RequestBody Delivery delivery) {
        //  Default value for productId
        delivery.setDeliveryId(deliveryRepository.generateDeliveryId());
        return deliveryRepository.save(delivery);
    }
    @PutMapping("/deliveries/{id}")
    public ResponseEntity<Delivery> updateBrand(@PathVariable String id, @RequestBody Delivery delivery) {
        Delivery _delevery = deliveryRepository.findByDeliveryId(id);
        if (_delevery == null)
            throw new ResourceNotFoundException("Delevery not found with id: " + id);

        _delevery.setLocation(delivery.getLocation());
        _delevery.setEmail(delivery.getEmail());
        _delevery.setName(delivery.getName());
        _delevery.setPhoneNumber(delivery.getPhoneNumber());
        deliveryRepository.save(_delevery);
        return ResponseEntity.ok(_delevery);
    }

    @DeleteMapping("/deliveries/{id}")
    public ResponseEntity<Delivery> deleteDelevery(@PathVariable String id) {
        Delivery delivery = deliveryRepository.findByDeliveryId(id);
        if (delivery == null)
            throw new ResourceNotFoundException("Delivery not found with id: " + id);
        deliveryRepository.delete(delivery);
        return ResponseEntity.ok(delivery);
    }
}
