package com.proyect.web.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Schema(name = "User", description = "Entidad que representa un usuario en el sistema")
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userName"}),
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"userImage"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Schema(description = "ID único del usuario", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre de usuario único", example = "john_doe", required = true)
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Schema(description = "Contraseña del usuario (encriptada)", example = "$2a$10$...", required = true)
    @Column(name = "password", nullable = false)
    private String password;

    @Schema(description = "Correo electrónico único del usuario", example = "john.doe@email.com", required = true)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Schema(description = "Número de teléfono del usuario", example = "+1234567890", required = true)
    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Schema(description = "URL o ruta de la imagen de perfil del usuario", example = "/images/profile/user1.jpg")
    @Column(name = "user_image", nullable = true)
    private String userImage;

    @Schema(description = "Indica si el usuario es un vendedor", example = "true")
    @Column(name = "seller", nullable = true)
    private boolean seller;

    @Schema(description = "Lista de productos asociados al usuario")
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    @Schema(description = "Roles asignados al usuario")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
    private Set<Rol> roles = new HashSet<>();

    @Schema(description = "Transacciones de compra realizadas por el usuario")
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> purchases = new HashSet<>();
}
