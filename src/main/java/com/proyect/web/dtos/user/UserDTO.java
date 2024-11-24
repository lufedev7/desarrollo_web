package com.proyect.web.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO para operaciones de creación y actualización de usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario", example = "john_doe",
            required = true, minLength = 3)
    @NotEmpty
    @Size(min = 3, message = "El nombre debe de tener almenos 3 letras")
    private String userName;

    @Schema(description = "Contraseña del usuario", example = "password123",
            accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Schema(description = "Correo electrónico del usuario", example = "john.doe@email.com",
            required = true)
    @Email
    private String email;

    @Schema(description = "Número de teléfono del usuario", example = "+1234567890")
    @PositiveOrZero
    private String phoneNumber;

    @Schema(description = "URL de la imagen de perfil del usuario",
            example = "/images/profile/user1.jpg")
    private String userImage;

    @Schema(description = "Indica si el usuario es vendedor", example = "false",
            defaultValue = "false")
    private boolean seller;
}