package com.proyect.web.controllers;

import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserPageResponseDTO;
import com.proyect.web.dtos.user.UserResponseDTO;
import com.proyect.web.responses.ApiResponse;
import com.proyect.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuarios", description = "API para la gestión de usuarios")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Verificar estado del servicio")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Servicio funcionando correctamente")
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(new ApiResponse<>("Servicio funcionando correctamente", "OK", true));
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema con los datos proporcionados")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de usuario inválidos")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El usuario ya existe")
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Parameter(description = "Datos del nuevo usuario")
            @Valid @RequestBody UserDTO userDTO) {
        UserResponseDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Usuario creado exitosamente", createdUser, true));
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista paginada de usuarios")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<UserPageResponseDTO>> getAllUsers(
            @Parameter(description = "Número de página (desde 0)")
            @RequestParam(defaultValue = "0") int pageNumber,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Campo por el cual ordenar")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Dirección del ordenamiento (ASC/DESC)")
            @RequestParam(defaultValue = "DESC") String sortDir) {
        UserPageResponseDTO pageResponse = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(new ApiResponse<>("Usuarios recuperados exitosamente", pageResponse, true),
                HttpStatus.OK);
    }

    @Operation(summary = "Obtener usuario por ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable(name = "id") Long id) {
        UserResponseDTO user = userService.findByUserId(id);
        return ResponseEntity.ok(new ApiResponse<>("Usuario encontrado", user, true));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable(name = "id") Long id,
            @Parameter(description = "Nuevos datos del usuario")
            @Valid @RequestBody UserDTO userDTO) {
        UserResponseDTO updatedUser = userService.updateUser(userDTO, id);
        return ResponseEntity.ok(new ApiResponse<>("Usuario actualizado exitosamente", updatedUser, true));
    }

    @Operation(summary = "Eliminar usuario")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "ID del usuario a eliminar", required = true)
            @PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>("Usuario eliminado exitosamente", null, true));
    }

    @Operation(summary = "Actualizar estado de vendedor",
            description = "Actualiza el estado de vendedor de un usuario")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estado de vendedor actualizado exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PatchMapping("/{userId}/seller")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateSellerStatus(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Nuevo estado de vendedor", required = true)
            @RequestParam boolean seller) {
        UserResponseDTO updatedUser = userService.updateSellerStatus(userId, seller);
        String message = seller ? "Usuario actualizado a vendedor exitosamente" :
                "Usuario removido de vendedor exitosamente";
        return ResponseEntity.ok(new ApiResponse<>(message, updatedUser, true));
    }

    @Operation(summary = "Obtener perfil del usuario actual",
            description = "Obtiene los datos del usuario autenticado")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Perfil recuperado exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/my-profile")
    public ResponseEntity<UserResponseDTO> getCurrentUserProfile() {
        UserResponseDTO userDto = userService.getCurrentUser();
        return ResponseEntity.ok(userDto);
    }
}