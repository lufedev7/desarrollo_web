package com.proyect.web.service;

import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.entitys.User;
import com.proyect.web.exceptions.UserAlreadyExistsException;
import com.proyect.web.exceptions.UserNotFoundException;
import com.proyect.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        // Validar username
        if (userDTO.getUserName() == null || userDTO.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es requerido");
        }
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new UserAlreadyExistsException("El nombre de usuario ya existe: " + userDTO.getUserName());
        }

        // Validar email
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("El email ya existe: " + userDTO.getEmail());
        }

        // Validar userImage si está presente
        if (userDTO.getUserImage() != null && !userDTO.getUserImage().trim().isEmpty()) {
            if (userRepository.existsByUserImage(userDTO.getUserImage())) {
                throw new UserAlreadyExistsException("La imagen de usuario ya existe");
            }
        }

        try {
            User user = mapearEntity(userDTO);
            User newUser = userRepository.save(user);
            return mapearDTO(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("Error al crear el usuario: datos duplicados");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::mapearDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));
        return mapearDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDTO, Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));

        // Validar username
        if (userRepository.existsByUserName(userDTO.getUserName()) &&
                !existingUser.getUserName().equals(userDTO.getUserName())) {
            throw new UserAlreadyExistsException("El nombre de usuario ya existe: " + userDTO.getUserName());
        }

        // Validar email
        if (userRepository.existsByEmail(userDTO.getEmail()) &&
                !existingUser.getEmail().equals(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("El email ya existe: " + userDTO.getEmail());
        }

        // Validar userImage
        if (userDTO.getUserImage() != null && !userDTO.getUserImage().equals(existingUser.getUserImage())) {
            if (userRepository.existsByUserImage(userDTO.getUserImage())) {
                throw new UserAlreadyExistsException("La imagen de usuario ya existe");
            }
        }

        try {
            existingUser.setUserName(userDTO.getUserName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());
            if (userDTO.getUserImage() != null) {
                existingUser.setUserImage(userDTO.getUserImage());
            }
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(userDTO.getPassword());
            }

            User updatedUser = userRepository.save(existingUser);
            return mapearDTO(updatedUser);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("Error al actualizar el usuario: datos duplicados");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    private User mapearEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO mapearDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
