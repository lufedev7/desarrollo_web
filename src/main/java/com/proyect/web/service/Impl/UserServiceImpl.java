package com.proyect.web.service.Impl;

import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserResponseDTO;
import com.proyect.web.entitys.Rol;
import com.proyect.web.entitys.User;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.exceptions.WebException;
import com.proyect.web.repository.RolRepository;
import com.proyect.web.repository.UserRepository;
import com.proyect.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserResponseDTO createUser(UserDTO userDTO) {

        User user = mapToEntity(userDTO);

        user.setEmail(userDTO.getEmail());
        user.setUserImage(userDTO.getUserImage());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserName(userDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setSeller(userDTO.isSeller());

        Rol roles = rolRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singleton(roles));
        User newUser = userRepository.save(user);
        return mapToResponseDTO(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll() {
        try {
            List<User> users = userRepository.findAll();
            if(users.isEmpty()) {
                throw new ResourceNotFoundException("Usuarios", "lista", 0);
            }
            return users.stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener la lista de usuarios");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));
        return mapToResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(UserDTO userDTO, Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));

        validateUpdateUser(userDTO, existingUser);

        try {
            updateUserFields(existingUser, userDTO);
            User updatedUser = userRepository.save(existingUser);
            return mapToResponseDTO(updatedUser);
        } catch (DataIntegrityViolationException e) {
            throw new WebException(HttpStatus.BAD_REQUEST, "Error al actualizar el usuario: datos duplicados");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "ID", id);
        }
        userRepository.deleteById(id);
    }

    private void validateNewUser(UserDTO userDTO) {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new WebException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new WebException(HttpStatus.BAD_REQUEST, "El email ya existe");
        }
        if (userDTO.getUserImage() != null && userRepository.existsByUserImage(userDTO.getUserImage())) {
            throw new WebException(HttpStatus.BAD_REQUEST, "La imagen de usuario ya existe");
        }
    }

    private void validateUpdateUser(UserDTO userDTO, User existingUser) {
        if (!existingUser.getUserName().equals(userDTO.getUserName())
                && userRepository.existsByUserName(userDTO.getUserName())) {
            throw new WebException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
        }
        if (!existingUser.getEmail().equals(userDTO.getEmail())
                && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new WebException(HttpStatus.BAD_REQUEST, "El email ya existe");
        }
        if (userDTO.getUserImage() != null
                && !userDTO.getUserImage().equals(existingUser.getUserImage())
                && userRepository.existsByUserImage(userDTO.getUserImage())) {
            throw new WebException(HttpStatus.BAD_REQUEST, "La imagen de usuario ya existe");
        }
    }

    private void updateUserFields(User user, UserDTO userDTO) {
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        if (userDTO.getUserImage() != null) {
            user.setUserImage(userDTO.getUserImage());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
    }

    @Override
    @Transactional
    public UserResponseDTO updateSellerStatus(Long userId, boolean seller) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", userId));

        // Opcionalmente, puedes agregar validaciones adicionales
        if (user.isSeller() == seller) {
            throw new WebException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario ya " + (seller ? "es" : "no es") + " vendedor"
            );
        }

        try {
            user.setSeller(seller);
            User updatedUser = userRepository.save(user);
            return mapToResponseDTO(updatedUser);
        } catch (Exception e) {
            throw new WebException(
                    HttpStatus.BAD_REQUEST,
                    "Error al actualizar el estado de vendedor del usuario"
            );
        }
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserImage(user.getUserImage());
        dto.setSeller(user.isSeller());
        return dto;
    }

    private User mapToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
