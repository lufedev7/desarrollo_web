package com.proyect.web.dtos.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {
    private String productName;
    private Long categoryId;
    private String productDescription;
    private Long userId;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;
    private Integer stockQuantity;
}
