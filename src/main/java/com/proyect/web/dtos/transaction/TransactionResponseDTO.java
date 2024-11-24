package com.proyect.web.dtos.transaction;

import com.proyect.web.entitys.PaymentMethod;
import com.proyect.web.entitys.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "Detalles de la transacción realizada")
public class TransactionResponseDTO {

    @Schema(description = "ID de la transacción", example = "1")
    private Long id;

    @Schema(description = "ID del comprador", example = "1")
    private Long buyerId;

    @Schema(description = "ID del producto", example = "1")
    private Long productId;

    @Schema(description = "Imágenes del producto")
    private List<String> imageUrls;

    @Schema(description = "Nombre del producto", example = "iPhone 13 Pro")
    private String productName;

    @Schema(description = "Nombre del comprador", example = "John Doe")
    private String userName;

    @Schema(description = "Precio de venta", example = "999.99")
    private BigDecimal salePrice;

    @Schema(description = "Fecha de la transacción")
    private LocalDateTime transactionDate;

    @Schema(description = "Estado de la transacción", example = "COMPLETED")
    private TransactionStatus status;

    @Schema(description = "Método de pago", example = "CREDIT_CARD")
    private PaymentMethod paymentMethod;
}