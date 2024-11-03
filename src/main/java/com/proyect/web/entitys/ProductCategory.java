package com.proyect.web.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_categories",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"categoryName"}),
        @UniqueConstraint(columnNames = {"categoryImage"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productCategoryId;

    @Column(nullable = false, length = 100)
    private String categoryName;

    private String categoryImage;

    @Column(columnDefinition = "TEXT")
    private String categoryDescription;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}
