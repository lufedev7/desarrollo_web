package com.proyect.web.repository;

import com.proyect.web.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    boolean existsByUserImage(String userImage);
    User findByUserName(String userName);
    User findByEmail(String email);
}
