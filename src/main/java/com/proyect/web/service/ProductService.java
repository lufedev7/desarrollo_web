package com.proyect.web.service;

import com.proyect.web.dtos.product.ProductCreateDTO;
import com.proyect.web.dtos.product.ProductDTO;
import com.proyect.web.dtos.product.ProductPageResponseDTO;
import com.proyect.web.dtos.product.ProductUpdateDTO;


public interface ProductService {
    ProductDTO createProduct(ProductCreateDTO productDTO);
    ProductDTO getProduct(Long id);
    ProductDTO updateProduct(Long id, ProductUpdateDTO productDTO);
    void deleteProduct(Long id, String currentUserEmail);
    ProductPageResponseDTO getProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductPageResponseDTO getProductsByUser(Long userId, int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductPageResponseDTO getProductsByUserEmail(String email, int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductDTO markProductAsSold(Long id, String currentUserEmail);
    ProductPageResponseDTO getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductPageResponseDTO searchProducts(String query, int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductPageResponseDTO getRelatedProducts(Long productId, int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductPageResponseDTO getMyProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
}
