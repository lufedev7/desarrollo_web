package com.proyect.web.service.Impl;

import com.proyect.web.dtos.category.ProductCategoryCreateDTO;
import com.proyect.web.dtos.category.ProductCategoryDTO;
import com.proyect.web.dtos.category.ProductCategoryUpdateDTO;
import com.proyect.web.entitys.ProductCategory;
import com.proyect.web.exceptions.DuplicateResourceException;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.repository.ProductCategoryRepository;
import com.proyect.web.service.ProductCategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository categoryRepository;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ProductCategoryDTO createCategory(ProductCategoryCreateDTO categoryDTO) {
        if (categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
            throw new DuplicateResourceException("Category name already exists");
        }
        if (categoryRepository.existsByCategoryImage(categoryDTO.getCategoryImage())) {
            throw new DuplicateResourceException("Category image already exists");
        }

        ProductCategory category = new ProductCategory();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCategoryImage(categoryDTO.getCategoryImage());
        category.setCategoryDescription(categoryDTO.getCategoryDescription());

        ProductCategory savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    @Override
    public ProductCategoryDTO getCategory(Long id) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return convertToDTO(category);
    }

    @Override
    public List<ProductCategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductCategoryDTO updateCategory(Long id, ProductCategoryUpdateDTO categoryDTO) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getCategoryName().equals(categoryDTO.getCategoryName()) &&
                categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
            throw new DuplicateResourceException("Category name already exists");
        }

        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCategoryDescription(categoryDTO.getCategoryDescription());

        ProductCategory updatedCategory = categoryRepository.save(category);
        return convertToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getProducts().isEmpty()) {
            throw new InvalidOperationException("Cannot delete category with associated products");
        }

        categoryRepository.deleteById(id);
    }

    private ProductCategoryDTO convertToDTO(ProductCategory category) {
        return new ProductCategoryDTO(
                category.getProductCategoryId(),
                category.getCategoryName(),
                category.getCategoryImage(),
                category.getCategoryDescription()
        );
    }
}

