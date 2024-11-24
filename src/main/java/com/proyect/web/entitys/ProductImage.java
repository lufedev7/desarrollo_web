package com.proyect.web.entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Entidad que representa una imagen de producto")
@Entity
@Table(name = "product_images",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"url"})})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductImage {

    @Schema(description = "ID único de la imagen", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Schema(description = "Producto al que pertenece la imagen")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Schema(description = "URL de la imagen",
            example = "/images/products/iphone-13-1.jpg",
            required = true)
    @Column(nullable = false)
    private String url;
}