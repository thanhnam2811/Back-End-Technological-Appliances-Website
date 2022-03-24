package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Get All Products
    @GetMapping("/products")
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //    Create product
    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        //  Default value for productId
        product.setProductId(productRepository.generateProductId());
        return productRepository.save(product);
    }

    //    Get product by id
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productRepository.findByProductId(id);
        if (product == null)
            throw new ResourceNotFoundException("Product not found with id: " + id);
        return ResponseEntity.ok(product);
    }

    //    Update product
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        Product _product = productRepository.findByProductId(id);
        if (_product == null)
            throw new ResourceNotFoundException("Product not found with id: " + id);
        _product.setName(product.getName());
        _product.setPrice(product.getPrice());
        _product.setDescription(product.getDescription());
        _product.setImage(product.getImage());
        _product.setCategory(product.getCategory());
        _product.setQuantity(product.getQuantity());
        _product.setBackCam(product.getBackCam());
        _product.setFrontCam(product.getFrontCam());
        _product.setRam(product.getRam());
        _product.setRom(product.getRom());
        _product.setWeight(product.getWeight());
        _product.setScreen(product.getScreen());
        _product.setOs(product.getOs());
        _product.setBattery(product.getBattery());
        _product.setCpu(product.getCpu());
        _product.setSaleDate(product.getSaleDate());
        _product.setVga(product.getVga());
        productRepository.save(_product);
        return ResponseEntity.ok(_product);
    }

    //    Delete product
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable String id) {
        Product product = productRepository.findByProductId(id);
        if (product == null)
            throw new ResourceNotFoundException("Product not found with id: " + id);
        productRepository.delete(product);
        return ResponseEntity.ok(product);
    }
}
