package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Get All Categories
    @GetMapping("categories")
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    //    Create category
    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
    //  Default value for categoryId
        category.setCategoryId(categoryRepository.generateCategoryId());
        return categoryRepository.save(category);
    }

    //    Get category by id
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        Category category = categoryRepository.findByCategoryId(id);
        if (category == null)
            throw new ResourceNotFoundException("Category not found with id: " + id);
        return ResponseEntity.ok(category);
    }

    //    Update category
    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
        Category _category = categoryRepository.findByCategoryId(id);
        if (_category == null)
            throw new ResourceNotFoundException("Category not found with id: " + id);
        _category.setName(category.getName());
        categoryRepository.save(_category);
        return ResponseEntity.ok(_category);
    }

    //    Delete category
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable String id) {
        Category category = categoryRepository.findByCategoryId(id);
        if (category == null)
            throw new ResourceNotFoundException("Category not found with id: " + id);
        categoryRepository.delete(category);
        return ResponseEntity.ok(category);
    }

}
