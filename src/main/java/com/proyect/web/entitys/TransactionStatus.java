package com.proyect.web.entitys;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de una transacción")
public enum TransactionStatus {
    @Schema(description = "Transacción pendiente de completar")
    PENDING,

    @Schema(description = "Transacción completada exitosamente")
    COMPLETED,

    @Schema(description = "Transacción cancelada")
    CANCELLED,

    @Schema(description = "Transacción reembolsada")
    REFUNDED
}