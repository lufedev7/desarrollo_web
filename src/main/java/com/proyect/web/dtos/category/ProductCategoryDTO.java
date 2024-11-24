package com.proyect.web.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO para operaciones de lectura de categorías de productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDTO {

    @Schema(description = "ID de la categoría", example = "1")
    private Long productCategoryId;

    @Schema(description = "Nombre de la categoría", example = "Electrónicos")
    private String categoryName;

    @Schema(description = "URL de la imagen de la categoría",
            example = "/images/categories/electronics.jpg")
    private String categoryImage;

    @Schema(description = "Descripción de la categoría",
            example = "Productos electrónicos, incluyendo smartphones, laptops y tablets")
    private String categoryDescription;
}