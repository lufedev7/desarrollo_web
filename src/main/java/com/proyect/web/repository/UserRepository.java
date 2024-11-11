package com.proyect.web.repository;

import com.proyect.web.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByUserName(String userName);
    public boolean existsByEmail(String email);
    public boolean existsByUserImage(String userImage);
    public Optional<User> findByUserNameOrEmail(String username, String email);
}
