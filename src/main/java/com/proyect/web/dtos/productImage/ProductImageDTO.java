package com.proyect.web.dtos.productImage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "DTO para operaciones de lectura de imágenes de producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {

    @Schema(description = "ID de la imagen",
            example = "1")
    private Long imageId;

    @Schema(description = "ID del producto asociado",
            example = "1")
    private Long productId;

    @Schema(description = "URL de la imagen",
            example = "/images/products/iphone-13-1.jpg")
    private String url;
}