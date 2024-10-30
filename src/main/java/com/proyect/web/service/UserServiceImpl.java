package com.proyect.web.service;

import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.entitys.User;
import com.proyect.web.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    private ModelMapper modelMapper;


    @Override
    public UserDTO createUser(UserDTO userDTO) {

        User user = mapearEntity(userDTO);

        User newUser = userRepository.save(user);
        return mapearDTO(newUser);
    }

    @Override
    public List<UserDTO> getAll() {
        return List.of();
    }

    @Override
    public UserDTO findByUserId(Long id) {
        return null;
    }

    @Override
    public UserDTO updateUser(UserDTO user, Long id) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {
    }


    private User mapearEntity(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO mapearDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
