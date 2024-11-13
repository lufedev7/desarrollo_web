package com.proyect.web.service.Impl;

import com.proyect.web.dtos.category.ProductCategoryCreateDTO;
import com.proyect.web.dtos.category.ProductCategoryDTO;
import com.proyect.web.dtos.category.ProductCategoryUpdateDTO;
import com.proyect.web.entitys.ProductCategory;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.exceptions.WebException;
import com.proyect.web.repository.ProductCategoryRepository;
import com.proyect.web.service.ProductCategoryService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository categoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductCategoryDTO createCategory(ProductCategoryCreateDTO categoryDTO) {
        validateNewCategory(categoryDTO);

        ProductCategory category = new ProductCategory();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCategoryImage(categoryDTO.getCategoryImage());
        category.setCategoryDescription(categoryDTO.getCategoryDescription());

        try {
            ProductCategory savedCategory = categoryRepository.save(category);
            return convertToDTO(savedCategory);
        } catch (Exception e) {
            throw new WebException(HttpStatus.BAD_REQUEST, "Error al crear la categoría");
        }
    }

    @Override
    @Transactional
    public ProductCategoryDTO getCategory(Long id) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "ID", id));
        return convertToDTO(category);
    }

    @Override
    @Transactional
    public List<ProductCategoryDTO> getAllCategories() {
        List<ProductCategory> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new ResourceNotFoundException("Categorías", "lista", 0);
        }
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductCategoryDTO updateCategory(Long id, ProductCategoryUpdateDTO categoryDTO) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "ID", id));

        validateUpdateCategory(categoryDTO, category);

        try {
            updateCategoryFields(category, categoryDTO);
            ProductCategory updatedCategory = categoryRepository.save(category);
            return convertToDTO(updatedCategory);
        } catch (Exception e) {
            throw new WebException(HttpStatus.BAD_REQUEST, "Error al actualizar la categoría");
        }
    }

    @Override
    public void deleteCategory(Long id) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "ID", id));

        if (!category.getProducts().isEmpty()) {
            throw new WebException(HttpStatus.BAD_REQUEST,
                    "No se puede eliminar la categoría porque tiene productos asociados");
        }

        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new WebException(HttpStatus.BAD_REQUEST, "Error al eliminar la categoría");
        }
    }

    private void validateNewCategory(ProductCategoryCreateDTO categoryDTO) {
        if (categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
            throw new WebException(HttpStatus.BAD_REQUEST,
                    "Ya existe una categoría con el nombre: " + categoryDTO.getCategoryName());
        }
        if (categoryRepository.existsByCategoryImage(categoryDTO.getCategoryImage())) {
            throw new WebException(HttpStatus.BAD_REQUEST,
                    "Ya existe una categoría con la imagen especificada");
        }
    }

    private void validateUpdateCategory(ProductCategoryUpdateDTO categoryDTO, ProductCategory category) {
        if (!category.getCategoryName().equals(categoryDTO.getCategoryName()) &&
                categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
            throw new WebException(HttpStatus.BAD_REQUEST,
                    "Ya existe una categoría con el nombre: " + categoryDTO.getCategoryName());
        }
    }

    private void updateCategoryFields(ProductCategory category, ProductCategoryUpdateDTO categoryDTO) {
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCategoryDescription(categoryDTO.getCategoryDescription());
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

