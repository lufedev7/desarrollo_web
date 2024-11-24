package com.proyect.web.entitys;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Métodos de pago disponibles")
public enum PaymentMethod {
    @Schema(description = "Pago con tarjeta de crédito")
    CREDIT_CARD,

    @Schema(description = "Pago con tarjeta de débito")
    DEBIT_CARD,

    @Schema(description = "Pago por transferencia bancaria")
    TRANSFER,

    @Schema(description = "Pago en efectivo")
    CASH
}