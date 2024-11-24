package com.proyect.web.entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Entidad que representa una categoría de productos")
@Entity
@Table(name = "product_categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"categoryName"}),
                @UniqueConstraint(columnNames = {"categoryImage"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {

    @Schema(description = "ID único de la categoría", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productCategoryId;

    @Schema(description = "Nombre de la categoría", example = "Electrónicos", required = true)
    @Column(nullable = false, length = 100)
    private String categoryName;

    @Schema(description = "URL de la imagen de la categoría",
            example = "/images/categories/electronics.jpg")
    private String categoryImage;

    @Schema(description = "Descripción detallada de la categoría",
            example = "Productos electrónicos, incluyendo smartphones, laptops y tablets")
    @Column(columnDefinition = "TEXT")
    private String categoryDescription;

    @Schema(description = "Lista de productos pertenecientes a esta categoría")
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}