package com.proyect.web.service;

import com.proyect.web.dtos.product.ProductPageResponseDTO;
import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserPageResponseDTO;
import com.proyect.web.dtos.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserDTO userDTO);
    UserPageResponseDTO getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
    UserResponseDTO findByUserId(Long id);
    UserResponseDTO updateUser(UserDTO userDTO, Long id);
    void deleteUser(Long id);
    UserResponseDTO updateSellerStatus(Long userId, boolean seller);
    UserResponseDTO getCurrentUser();
    UserPageResponseDTO searchUsers(String query, int pageNumber, int pageSize, String sortBy, String sortDir);
}
