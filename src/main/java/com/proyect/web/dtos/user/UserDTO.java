package com.proyect.web.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotEmpty
    @Size(min = 3, message = "El nombre debe de tener almenos 3 letras")
    private String userName;
    private String password;
    @Email
    private String email;
    @PositiveOrZero
    private String phoneNumber;
    private String userImage;
    private boolean seller;
}
