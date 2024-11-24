package com.proyect.web.dtos.productImage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO para crear una nueva imagen de producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageCreateDTO {

    @Schema(description = "ID del producto al que se asociará la imagen",
            example = "1",
            required = true)
    @NotNull(message = "El ID del producto es requerido")
    private Long productId;

    @Schema(description = "URL de la imagen",
            example = "/images/products/iphone-13-1.jpg",
            required = true)
    @NotNull(message = "La URL de la imagen es requerida")
    @Pattern(regexp = "^/images/.*|^https?://.*",
            message = "La URL debe comenzar con '/images/' o ser una URL válida")
    private String url;
}