package com.proyect.web.controllers;

import com.proyect.web.dtos.productImage.*;
import com.proyect.web.exceptions.*;
import com.proyect.web.service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Imágenes de Productos", description = "API para la gestión de imágenes de productos")
@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;

    @Autowired
    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @Operation(summary = "Crear nueva imagen de producto",
            description = "Agrega una nueva imagen a un producto existente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Imagen creada exitosamente",
            content = @Content(schema = @Schema(implementation = ProductImageDTO.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "URL de imagen inválida o duplicada")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado")
    @PostMapping
    public ResponseEntity<ProductImageDTO> createProductImage(
            @Parameter(description = "Datos de la nueva imagen", required = true)
            @RequestBody ProductImageCreateDTO productImageDTO) {
        return new ResponseEntity<>(productImageService.createProductImage(productImageDTO),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener imagen por ID",
            description = "Obtiene los detalles de una imagen específica")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Imagen encontrada",
            content = @Content(schema = @Schema(implementation = ProductImageDTO.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Imagen no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ProductImageDTO> getProductImage(
            @Parameter(description = "ID de la imagen", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(productImageService.getProductImage(id));
    }

    @Operation(summary = "Obtener todas las imágenes",
            description = "Obtiene la lista de todas las imágenes de productos")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de imágenes recuperada exitosamente",
            content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = ProductImageDTO.class))))
    @GetMapping
    public ResponseEntity<List<ProductImageDTO>> getAllProductImages() {
        return ResponseEntity.ok(productImageService.getAllProductImages());
    }

    @Operation(summary = "Obtener imágenes por producto",
            description = "Obtiene todas las imágenes asociadas a un producto específico")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Imágenes del producto recuperadas exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImageDTO>> getImagesByProduct(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long productId) {
        return ResponseEntity.ok(productImageService.getImagesByProduct(productId));
    }

    @Operation(summary = "Actualizar imágenes de producto",
            description = "Actualiza la lista completa de imágenes de un producto")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Imágenes actualizadas exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<List<ProductImageDTO>> updateProductImages(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long productId,
            @Parameter(description = "Lista de nuevas URLs de imágenes", required = true)
            @RequestBody List<String> newUrls) {
        return ResponseEntity.ok(productImageService.updateProductImages(productId, newUrls));
    }

    @Operation(summary = "Eliminar imagen",
            description = "Elimina una imagen específica de un producto")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Imagen eliminada exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Imagen no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProductImage(
            @Parameter(description = "ID de la imagen a eliminar", required = true)
            @PathVariable Long id) {
        productImageService.deleteProductImage(id);
        return ResponseEntity.ok(Map.of("message", "Imagen eliminada exitosamente"));
    }

    @Operation(summary = "Eliminar todas las imágenes de un producto",
            description = "Elimina todas las imágenes asociadas a un producto específico")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Imágenes eliminadas exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado")
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Map<String, String>> deleteAllProductImages(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long productId) {
        productImageService.deleteAllProductImages(productId);
        return ResponseEntity.ok(Map.of(
                "message", "Todas las imágenes del producto han sido eliminadas"));
    }
}