package com.proyect.web.controllers;

import com.proyect.web.dtos.product.ProductCreateDTO;
import com.proyect.web.dtos.product.ProductDTO;
import com.proyect.web.dtos.product.ProductPageResponseDTO;
import com.proyect.web.dtos.product.ProductUpdateDTO;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.responses.ApiResponse;
import com.proyect.web.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import utils.AppConstants;

import java.util.Map;

@Tag(name = "Productos", description = "API para la gestión de productos")
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //BASIC CRUD

    @Operation(summary = "Crear nuevo producto",
            description = "Crea un nuevo producto en el sistema")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente"
    )
    @PostMapping("/new")
    public ResponseEntity<ProductDTO> createProduct(
            @Parameter(description = "Datos del nuevo producto", required = true)
            @RequestBody ProductCreateDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidOperationException("Error al crear el producto: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener productos paginados",
            description = "Obtiene una lista paginada de todos los productos")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getAllProductsPaginated(
            @Parameter(description = "Número de página")
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @Parameter(description = "Tamaño de página")
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @Parameter(description = "Campo para ordenar")
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @Parameter(description = "Dirección del ordenamiento")
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(
                new ApiResponse<>("Productos recuperados exitosamente", response, true)
        );
    }

    @Operation(summary = "Actualizar producto",
            description = "Actualiza un producto existente")
    @PutMapping("update/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del producto")
            @RequestBody ProductUpdateDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (ResourceNotFoundException | InvalidOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidOperationException("Error al actualizar el producto: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar producto",
            description = "Elimina un producto del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(
            @Parameter(description = "ID del producto a eliminar", required = true)
            @PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new InvalidOperationException("Usuario no autenticado");
            }

            String currentUserEmail = authentication.getName();
            productService.deleteProduct(id, currentUserEmail);

            Map<String, String> response = Map.of(
                    "message", "Producto eliminado exitosamente",
                    "status", "success"
            );

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar el producto: " + e.getMessage()
            );
        }
    }

    //ADDITIONALS

    @Operation(summary = "Obtener producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id) {
        try {
            ProductDTO product = productService.getProduct(id);
            return ResponseEntity.ok(product);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Obtener productos por usuario",
            description = "Obtiene una lista paginada de productos de un usuario específico")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getProductsByUser(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Parámetros de paginación")
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

    @Operation(summary = "Obtener productos por categoría",
            description = "Obtiene una lista paginada de productos de una categoría específica")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
            description = "Productos recuperados exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
            description = "Categoría no encontrada")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getProductsByCategory(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long categoryId,
            @Parameter(description = "Número de página")
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @Parameter(description = "Tamaño de página")
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @Parameter(description = "Campo para ordenar")
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @Parameter(description = "Dirección del ordenamiento (ASC/DESC)")
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.getProductsByCategory(
                categoryId, pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(
                new ApiResponse<>("Productos por categoría recuperados exitosamente", response, true)
        );
    }

    @Operation(summary = "Marcar producto como vendido",
            description = "Actualiza el estado de un producto a vendido")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
            description = "Producto marcado como vendido exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403",
            description = "Usuario no autorizado para realizar esta acción")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
            description = "Producto no encontrado")
    @PatchMapping("/{id}/sold")
    public ResponseEntity<ProductDTO> markProductAsSold(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new InvalidOperationException("Usuario no autenticado");
            }

            String currentUserEmail = authentication.getName();
            ProductDTO product = productService.markProductAsSold(id, currentUserEmail);
            return ResponseEntity.ok(product);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating product status");
        }
    }

    @Operation(summary = "Obtener productos relacionados",
            description = "Obtiene una lista paginada de productos relacionados con un producto específico")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
            description = "Productos relacionados encontrados exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
            description = "Producto no encontrado")
    @GetMapping("/{productId}/related")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getRelatedProducts(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long productId,
            @Parameter(description = "Número de página")
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @Parameter(description = "Tamaño de página")
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @Parameter(description = "Campo para ordenar")
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @Parameter(description = "Dirección del ordenamiento")
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.getRelatedProducts(
                productId, pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(
                new ApiResponse<>("Productos relacionados encontrados exitosamente", response, true)
        );
    }

    @Operation(summary = "Obtener mis productos",
            description = "Obtiene una lista paginada de los productos del usuario autenticado")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
            description = "Productos recuperados exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
            description = "Usuario no autenticado")
    @GetMapping("/my-products")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> getMyProducts(
            @Parameter(description = "Número de página")
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @Parameter(description = "Tamaño de página")
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @Parameter(description = "Campo para ordenar")
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @Parameter(description = "Dirección del ordenamiento")
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

    @Operation(summary = "Buscar productos",
            description = "Busca productos por término de búsqueda")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
            description = "Búsqueda realizada exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
            description = "Parámetros de búsqueda inválidos")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ProductPageResponseDTO>> searchProducts(
            @Parameter(description = "Término de búsqueda", required = true)
            @RequestParam String query,
            @Parameter(description = "Número de página")
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @Parameter(description = "Tamaño de página")
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @Parameter(description = "Campo para ordenar")
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @Parameter(description = "Dirección del ordenamiento")
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        if (pageSize < 1) pageSize = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
        if (pageNumber < 0) pageNumber = 0;

        ProductPageResponseDTO response = productService.searchProducts(
                query, pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(
                new ApiResponse<>("Productos encontrados exitosamente", response, true)
        );
    }
}

