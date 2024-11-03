package com.proyect.web.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {
    private String productName;
    private Long categoryId;
    private String productDescription;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;
    private Integer stockQuantity;
}
