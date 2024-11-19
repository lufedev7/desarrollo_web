package com.proyect.web.dtos.productImage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageCreateDTO {
    private Long productId;
    private String url;
}
