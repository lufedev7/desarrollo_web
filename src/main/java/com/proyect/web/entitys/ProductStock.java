package com.proyect.web.entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Entidad que representa el stock de un producto")
@Entity
@Table(name = "product_stocks")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductStock {

    @Schema(description = "ID único del stock", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @Schema(description = "Cantidad disponible del producto",
            example = "10",
            minimum = "0")
    @Column(nullable = false)
    private Integer quantity;

    @Schema(description = "Producto asociado al stock")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}