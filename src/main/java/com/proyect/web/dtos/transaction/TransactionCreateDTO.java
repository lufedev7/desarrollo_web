package com.proyect.web.dtos.transaction;

import com.proyect.web.entitys.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Solicitud de creación de transacción")
public class TransactionCreateDTO {

    @Schema(description = "Lista de productos a comprar", required = true)
    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    private List<TransactionItemDTO> items;

    @Schema(description = "Método de pago", example = "CREDIT_CARD", required = true)
    @NotNull(message = "El método de pago es requerido")
    private PaymentMethod paymentMethod;
}