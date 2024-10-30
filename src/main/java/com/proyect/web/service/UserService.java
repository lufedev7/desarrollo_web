package com.proyect.web.service;

import com.proyect.web.dtos.user.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public UserDTO createUser(UserDTO userDTO);

    public List<UserDTO> getAll();

    public UserDTO findByUserId(Long id);

    public UserDTO updateUser(UserDTO userDTO, Long id);

    public void deleteUser(Long id);
}
