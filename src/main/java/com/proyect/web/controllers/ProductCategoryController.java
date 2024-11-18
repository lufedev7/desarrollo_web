package com.proyect.web.controllers;

import com.proyect.web.dtos.category.ProductCategoryCreateDTO;
import com.proyect.web.dtos.category.ProductCategoryDTO;
import com.proyect.web.dtos.category.ProductCategoryUpdateDTO;
import com.proyect.web.exceptions.DuplicateResourceException;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductCategoryController {
    private final ProductCategoryService categoryService;

    @Autowired
    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/new")
    public ResponseEntity<ProductCategoryDTO> createCategory(@RequestBody ProductCategoryCreateDTO categoryDTO) {
        try {
            ProductCategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (DuplicateResourceException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating category");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> getCategory(@PathVariable Long id) {
        try {
            ProductCategoryDTO category = categoryService.getCategory(id);
            return ResponseEntity.ok(category);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getAllCategories() {
        List<ProductCategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody ProductCategoryUpdateDTO categoryDTO) {
        try {
            ProductCategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateResourceException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating category");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            Map<String, String> response = Map.of("message", "Category deleted successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting category");
        }
    }
}