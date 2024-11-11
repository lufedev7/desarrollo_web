package com.proyect.web.controllers;

import com.proyect.web.dtos.product.ProductCreateDTO;
import com.proyect.web.dtos.product.ProductDTO;
import com.proyect.web.dtos.product.ProductPageResponseDTO;
import com.proyect.web.dtos.product.ProductUpdateDTO;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.responses.ApiResponse;
import com.proyect.web.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utils.AppConstants;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/new")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating product");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProduct(id);
            return ResponseEntity.ok(product);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Nuevo endpoint para paginación
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getAllProductsPaginated(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        // Validación de parámetros
        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(
                new ApiResponse<>("Productos recuperados exitosamente", response, true)
        );
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating product");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            Map<String, String> response = Map.of("message", "Product deleted successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting product");
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.getProductsByCategory(
                categoryId, pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(
                new ApiResponse<>("Productos por categoría recuperados exitosamente", response, true)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getProductsByUser(
            @PathVariable Long userId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.getProductsByUser(
                userId, pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(
                new ApiResponse<>("Productos del usuario recuperados exitosamente", response, true)
        );
    }

    // Para obtener los productos del usuario autenticado
    @GetMapping("/my-products")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getMyProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.getProductsByUserEmail(
                currentUserEmail, pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(
                new ApiResponse<>("Mis productos recuperados exitosamente", response, true)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> searchProducts(
            @RequestParam(required = true) String query,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.searchProducts(
                query, pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(
                new ApiResponse<>("Productos encontrados exitosamente", response, true)
        );
    }

    @GetMapping("/{productId}/related")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getRelatedProducts(
            @PathVariable Long productId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.getRelatedProducts(
                productId, pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(
                new ApiResponse<>("Productos relacionados encontrados exitosamente", response, true)
        );
    }

    @PatchMapping("/{id}/sold")
    public ResponseEntity<ProductDTO> markProductAsSold(@PathVariable Long id) {
        try {
            ProductDTO product = productService.markProductAsSold(id);
            return ResponseEntity.ok(product);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating product status");
        }
    }
}

