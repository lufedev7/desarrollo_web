package com.proyect.web.service;

import com.proyect.web.dtos.category.ProductCategoryCreateDTO;
import com.proyect.web.dtos.category.ProductCategoryDTO;
import com.proyect.web.dtos.category.ProductCategoryUpdateDTO;

import java.util.List;

public interface ProductCategoryService {
    ProductCategoryDTO createCategory(ProductCategoryCreateDTO categoryDTO);
    ProductCategoryDTO getCategory(Long id);
    List<ProductCategoryDTO> getAllCategories();
    ProductCategoryDTO updateCategory(Long id, ProductCategoryUpdateDTO categoryDTO);
    void deleteCategory(Long id);
}
