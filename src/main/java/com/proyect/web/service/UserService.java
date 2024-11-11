package com.proyect.web.service;

import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDTO createUser(UserDTO userDTO);

    List<UserResponseDTO> getAll();

    UserResponseDTO findByUserId(Long id);

    UserResponseDTO updateUser(UserDTO userDTO, Long id);

    void deleteUser(Long id);

    UserResponseDTO updateSellerStatus(Long userId, boolean seller);
}
