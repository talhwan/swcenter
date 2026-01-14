package com.thc.sprbasic2025.repository;

import com.thc.sprbasic2025.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    User findByCode(String code);
    User findByNick(String nick);
    //Optional<User> findByUsernameAndPassword(String username, String password);
}
