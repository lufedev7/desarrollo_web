package com.proyect.web.dtos.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDTO {
    private Long productCategoryId;
    private String categoryName;
    private String categoryImage;
    private String categoryDescription;
}
