package com.proyect.web.controllers;

import com.proyect.web.dtos.productImage.ProductImageDTO;
import com.proyect.web.dtos.productImage.ProductImageCreateDTO;
import com.proyect.web.dtos.productImage.ProductImageUpdateDTO;
import com.proyect.web.entitys.Product;
import com.proyect.web.entitys.ProductImage;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.repository.ProductImageRepository;
import com.proyect.web.repository.ProductRepository;
import com.proyect.web.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;
    private ProductImageRepository productImageRepository;
    private ProductRepository productRepository;

    @Autowired
    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping
    public ResponseEntity<ProductImageDTO> createProductImage(@RequestBody ProductImageCreateDTO productImageDTO) {
        return new ResponseEntity<>(productImageService.createProductImage(productImageDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageDTO> getProductImage(@PathVariable Long id) {
        return ResponseEntity.ok(productImageService.getProductImage(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductImageDTO>> getAllProductImages() {
        return ResponseEntity.ok(productImageService.getAllProductImages());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImageDTO>> getImagesByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productImageService.getImagesByProduct(productId));
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<List<ProductImageDTO>> updateProductImages(
            @PathVariable Long productId,
            @RequestBody List<String> newUrls) {
        return ResponseEntity.ok(productImageService.updateProductImages(productId, newUrls));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProductImage(@PathVariable Long id) {
        productImageService.deleteProductImage(id);
        return ResponseEntity.ok(Map.of("message", "Imagen eliminada exitosamente"));
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Map<String, String>> deleteAllProductImages(@PathVariable Long productId) {
        productImageService.deleteAllProductImages(productId);
        return ResponseEntity.ok(Map.of("message", "Todas las imágenes del producto han sido eliminadas"));
    }
}
