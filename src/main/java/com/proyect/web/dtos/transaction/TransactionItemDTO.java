package com.proyect.web.dtos.transaction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Item individual para la compra")
public class TransactionItemDTO {

    @Schema(description = "ID del producto a comprar", example = "1", required = true)
    @NotNull(message = "El ID del producto es requerido")
    private Long productId;

    @Schema(description = "Cantidad a comprar", example = "1", required = true)
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer quantity;
}
