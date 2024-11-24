package com.proyect.web.controllers;

import com.proyect.web.dtos.category.ProductCategoryCreateDTO;
import com.proyect.web.dtos.category.ProductCategoryDTO;
import com.proyect.web.dtos.category.ProductCategoryUpdateDTO;
import com.proyect.web.exceptions.DuplicateResourceException;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Tag(name = "Categorías", description = "API para la gestión de categorías de productos")
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductCategoryController {
    private final ProductCategoryService categoryService;

    @Autowired
    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Crear nueva categoría",
            description = "Crea una nueva categoría de productos en el sistema")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Categoría creada exitosamente",
            content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "La categoría ya existe")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor")
    @PostMapping("/new")
    public ResponseEntity<ProductCategoryDTO> createCategory(
            @Parameter(description = "Datos de la nueva categoría", required = true)
            @RequestBody ProductCategoryCreateDTO categoryDTO) {
        try {
            ProductCategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (DuplicateResourceException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating category");
        }
    }

    @Operation(summary = "Obtener categoría por ID",
            description = "Obtiene los detalles de una categoría específica")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Categoría encontrada",
            content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> getCategory(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        try {
            ProductCategoryDTO category = categoryService.getCategory(id);
            return ResponseEntity.ok(category);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Obtener todas las categorías",
            description = "Obtiene la lista de todas las categorías disponibles")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de categorías recuperada exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductCategoryDTO.class))))
    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getAllCategories() {
        List<ProductCategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Actualizar categoría",
            description = "Actualiza los datos de una categoría existente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Categoría actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Conflicto con datos existentes")
    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> updateCategory(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados de la categoría", required = true)
            @RequestBody ProductCategoryUpdateDTO categoryDTO) {
        try {
            ProductCategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateResourceException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating category");
        }
    }

    @Operation(summary = "Eliminar categoría",
            description = "Elimina una categoría existente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Categoría eliminada exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "No se puede eliminar la categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(
            @Parameter(description = "ID de la categoría a eliminar", required = true)
            @PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            Map<String, String> response = Map.of("message", "Category deleted successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting category");
        }
    }
}