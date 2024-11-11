package com.proyect.web.controllers;

import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserResponseDTO;
import com.proyect.web.responses.ApiResponse;
import com.proyect.web.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(new ApiResponse<>("Servicio funcionando correctamente", "OK", true));
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserResponseDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Usuario creado exitosamente", createdUser, true));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAll();
        return ResponseEntity.ok(
                new ApiResponse<>("Usuarios recuperados exitosamente", users, true));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable(name = "id") Long id) {
        UserResponseDTO user = userService.findByUserId(id);
        return ResponseEntity.ok(
                new ApiResponse<>("Usuario encontrado", user, true));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@PathVariable(name = "id") Long id,
                                                                   @Valid @RequestBody UserDTO userDTO) {
        UserResponseDTO updatedUser = userService.updateUser(userDTO, id);
        return ResponseEntity.ok(
                new ApiResponse<>("Usuario actualizado exitosamente", updatedUser, true));
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                new ApiResponse<>("Usuario eliminado exitosamente", null, true));
    }

    @PatchMapping("/{userId}/seller")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateSellerStatus(
            @PathVariable Long userId,
            @RequestParam boolean seller) {

        UserResponseDTO updatedUser = userService.updateSellerStatus(userId, seller);
        String message = seller ? "Usuario actualizado a vendedor exitosamente" :
                "Usuario removido de vendedor exitosamente";

        return ResponseEntity.ok(
                new ApiResponse<>(message, updatedUser, true)
        );
    }
}