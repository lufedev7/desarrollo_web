package com.proyect.web.entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Entidad que representa un rol de usuario")
@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rol {

    @Schema(description = "ID único del rol", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Schema(description = "Nombre del rol", example = "ROLE_USER")
    @Column(length = 160)
    private String name;

    @Schema(description = "Usuarios que tienen este rol")
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}