package com.proyect.web.service;

import com.proyect.web.dtos.productImage.ProductImageDTO;
import com.proyect.web.dtos.productImage.ProductImageCreateDTO;
import com.proyect.web.dtos.productImage.ProductImageUpdateDTO;
import java.util.List;

public interface ProductImageService {
    ProductImageDTO createProductImage(ProductImageCreateDTO productImageDTO);
    ProductImageDTO getProductImage(Long id);
    List<ProductImageDTO> getAllProductImages();
    List<ProductImageDTO> getImagesByProduct(Long productId);
    List<ProductImageDTO> updateProductImages(Long productId, List<String> newUrls);
    void deleteProductImage(Long id);
    void deleteAllProductImages(Long productId);
}
