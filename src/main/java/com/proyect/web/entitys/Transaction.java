package com.proyect.web.entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Entidad que representa una transacción de compra/venta")
@Entity
@Table(name = "transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Schema(description = "ID único de la transacción", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Schema(description = "Usuario comprador")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @Schema(description = "Producto involucrado en la transacción")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Schema(description = "Fecha y hora de la transacción",
            example = "2024-11-23T10:30:00")
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Schema(description = "Estado actual de la transacción",
            example = "COMPLETED")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Schema(description = "Método de pago utilizado",
            example = "CREDIT_CARD")
    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @PrePersist
    public void prePersist() {
        this.transactionDate = LocalDateTime.now();
    }
}