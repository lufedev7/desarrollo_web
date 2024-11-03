package com.proyect.web.dtos.product;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private Long categoryId;
    private String productDescription;
    private Boolean isNew;
    private Long userId;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;
    private Boolean isSold;
    private List<String> imageUrls;
    private Integer stockQuantity;
}
