package com.proyect.web.entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Schema(name = "Product", description = "Entidad que representa un producto en el sistema")
@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Schema(description = "ID único del producto", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Schema(description = "Nombre del producto", example = "iPhone 13 Pro", required = true)
    @Column(nullable = false, length = 100)
    private String productName;

    @Schema(description = "Categoría del producto")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
    private ProductCategory category;

    @Schema(description = "Descripción detallada del producto",
            example = "Smartphone Apple iPhone 13 Pro con 256GB de almacenamiento...")
    @Column(columnDefinition = "TEXT")
    private String productDescription;

    @Schema(description = "Indica si el producto es nuevo", example = "true", defaultValue = "true")
    private Boolean isNew = true;

    @Schema(description = "Usuario vendedor del producto")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Schema(description = "Precio original del producto", example = "999.99", required = true)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Schema(description = "Precio de venta (con descuento si aplica)", example = "899.99")
    @Column(precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Schema(description = "Indica si el producto ha sido vendido", example = "false", defaultValue = "false")
    private Boolean isSold = false;

    @Schema(description = "Lista de imágenes del producto")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @Schema(description = "Información del stock del producto")
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductStock stock;

    @Schema(description = "Historial de transacciones del producto")
    @OneToMany(mappedBy = "product")
    private Set<Transaction> transactions = new HashSet<>();
}