package com.proyect.web.dtos.productImage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO para actualizar una imagen de producto existente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageUpdateDTO {

    @Schema(description = "Nueva URL de la imagen",
            example = "/images/products/iphone-13-updated.jpg",
            required = true)
    @NotNull(message = "La URL de la imagen es requerida")
    @Pattern(regexp = "^/images/.*|^https?://.*",
            message = "La URL debe comenzar con '/images/' o ser una URL válida")
    private String url;
}