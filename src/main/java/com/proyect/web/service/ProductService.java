package com.proyect.web.service;

import com.proyect.web.dtos.product.ProductCreateDTO;
import com.proyect.web.dtos.product.ProductDTO;
import com.proyect.web.dtos.product.ProductUpdateDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductCreateDTO productDTO);
    ProductDTO getProduct(Long id);
    List<ProductDTO> getAllProducts();
    ProductDTO updateProduct(Long id, ProductUpdateDTO productDTO);
    void deleteProduct(Long id);
    List<ProductDTO> getProductsByCategory(Long categoryId);
    List<ProductDTO> getProductsByUser(Long userId);
    ProductDTO markProductAsSold(Long id);
}
