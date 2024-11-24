package com.proyect.web.service.Impl;

import com.proyect.web.dtos.product.ProductDTO;
import com.proyect.web.dtos.product.ProductPageResponseDTO;
import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserPageResponseDTO;
import com.proyect.web.dtos.user.UserResponseDTO;
import com.proyect.web.entitys.Product;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    public UserPageResponseDTO getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> pageUsers = userRepository.findAll(pageable);
        List<User> users = pageUsers.getContent();

        List<UserResponseDTO> content = users.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        UserPageResponseDTO userPageResponseDTO = new UserPageResponseDTO();
        userPageResponseDTO.setContent(content);
        userPageResponseDTO.setPageNo(pageUsers.getNumber());
        userPageResponseDTO.setPageSize(pageUsers.getSize());
        userPageResponseDTO.setTotalElements(pageUsers.getTotalElements());
        userPageResponseDTO.setTotalPages(pageUsers.getTotalPages());
        userPageResponseDTO.setLast(pageUsers.isLast());

        return userPageResponseDTO;
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // Limpiar transacciones
        user.getPurchases().clear();

        // Limpiar productos
        user.getProducts().forEach(product -> {
            product.getTransactions().clear();
            product.getImages().clear();
            if(product.getStock() != null) {
                product.setStock(null);
            }
        });
        user.getProducts().clear();

        // Limpiar roles
        user.getRoles().clear();

        userRepository.delete(user);
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

    public UserResponseDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUserNameOrEmail(username, username);
        User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("Usuario", "username/email", 0));
        return mapToResponseDTO(user);
    }

    @Override
    public UserPageResponseDTO searchUsers(String query, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> pageUsers = userRepository.findByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query, pageable);

        List<User> users = pageUsers.getContent();

        if(users.isEmpty()) {
            throw new ResourceNotFoundException("Usuarios", "búsqueda", 0);
        }

        List<UserResponseDTO> content = users.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        UserPageResponseDTO userPageResponseDTO = new UserPageResponseDTO();
        userPageResponseDTO.setContent(content);
        userPageResponseDTO.setPageNo(pageUsers.getNumber());
        userPageResponseDTO.setPageSize(pageUsers.getSize());
        userPageResponseDTO.setTotalElements(pageUsers.getTotalElements());
        userPageResponseDTO.setTotalPages(pageUsers.getTotalPages());
        userPageResponseDTO.setLast(pageUsers.isLast());

        return userPageResponseDTO;
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
