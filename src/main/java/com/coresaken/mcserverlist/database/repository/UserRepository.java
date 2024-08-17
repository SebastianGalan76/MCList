package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailOrLogin(String email, String login);

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
}
