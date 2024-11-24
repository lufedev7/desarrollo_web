package com.proyect.web.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO para actualizar una categoría de productos existente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryUpdateDTO {

    @Schema(description = "Nuevo nombre de la categoría", example = "Electrónicos")
    private String categoryName;

    @Schema(description = "Nueva descripción de la categoría",
            example = "Productos electrónicos, incluyendo smartphones, laptops y tablets")
    private String categoryDescription;
}
