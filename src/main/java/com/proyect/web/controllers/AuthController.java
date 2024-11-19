package com.proyect.web.controllers;


import com.proyect.web.dtos.otherFuntionalities.LoginDTO;
import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserResponseDTO;
import com.proyect.web.entitys.User;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.repository.UserRepository;
import com.proyect.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
   @Autowired
   private AuthenticationManager authenticationManager;
   @Autowired
   private UserRepository userRepository;
   @Autowired
   private UserService userServices;

   @PostMapping("/login")
   public ResponseEntity<UserResponseDTO> authenticationUser(@RequestBody LoginDTO loginDTO) {
      // Esta parte se mantiene igual
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Obtener el usuario actual después de la autenticación
      User user = userRepository.findByUserNameOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail())
              .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email/username", 0));

      // Convertir a DTO
      UserResponseDTO responseDTO = new UserResponseDTO(
              user.getId(),
              user.getUserName(),
              user.getEmail(),
              user.getPhoneNumber(),
              user.getUserImage(),
              user.isSeller()
      );

      return new ResponseEntity<>(responseDTO, HttpStatus.OK);
   }

   @PostMapping("/register")
   public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
      if (userRepository.existsByUserName(userDTO.getUserName())) {
         return new ResponseEntity<>("this username already exist", HttpStatus.BAD_REQUEST);
      }
      if (userRepository.existsByEmail(userDTO.getEmail())) {
         return new ResponseEntity<>("This email already exist", HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(userServices.createUser(userDTO), HttpStatus.CREATED);
   }
}
