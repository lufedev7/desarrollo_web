package com.proyect.web.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO para respuestas con información de usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario", example = "john_doe")
    private String userName;

    @Schema(description = "Correo electrónico del usuario", example = "john.doe@email.com")
    private String email;

    @Schema(description = "Número de teléfono del usuario", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "URL de la imagen de perfil del usuario",
            example = "/images/profile/user1.jpg")
    private String userImage;

    @Schema(description = "Indica si el usuario es vendedor", example = "false")
    private boolean seller;
}