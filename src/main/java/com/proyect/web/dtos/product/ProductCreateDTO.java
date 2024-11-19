package com.proyect.web.dtos.product;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {
    private String productName;
    private Long categoryId;
    private String productDescription;
    private Boolean isNew;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;
    private List<String> imageUrls;
    private Integer stockQuantity;
}
