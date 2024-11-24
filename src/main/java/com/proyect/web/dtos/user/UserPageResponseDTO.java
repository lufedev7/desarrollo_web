package com.proyect.web.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "Respuesta paginada de usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPageResponseDTO {

    @Schema(description = "Lista de usuarios en la página actual")
    private List<UserResponseDTO> content;

    @Schema(description = "Número de página actual", example = "0")
    private int pageNo;

    @Schema(description = "Tamaño de la página", example = "10")
    private int pageSize;

    @Schema(description = "Total de elementos en todas las páginas", example = "100")
    private long totalElements;

    @Schema(description = "Total de páginas disponibles", example = "10")
    private int totalPages;

    @Schema(description = "Indica si es la última página", example = "false")
    private boolean last;
}