package com.proyect.web.repository;

import com.proyect.web.entitys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByUserName(String userName);
    public boolean existsByEmail(String email);
    public boolean existsByUserImage(String userImage);
    public Optional<User> findByUserNameOrEmail(String username, String email);
    Page<User> findByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String userName, String email, Pageable pageable);
}
