package com.proyect.web.dtos.productImage;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {
    private Long imageId;
    private Long productId;
    private String url;
}
