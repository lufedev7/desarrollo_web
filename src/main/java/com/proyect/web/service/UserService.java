package com.proyect.web.service;

import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserDTO userDTO);

    List<UserResponseDTO> getAll();

    UserResponseDTO findByUserId(Long id);

    UserResponseDTO updateUser(UserDTO userDTO, Long id);

    void deleteUser(Long id);

    UserResponseDTO updateSellerStatus(Long userId, boolean seller);

    UserResponseDTO getCurrentUser();
}
