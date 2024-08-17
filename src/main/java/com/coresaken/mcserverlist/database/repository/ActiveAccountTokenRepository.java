package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.ActiveAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ActiveAccountTokenRepository extends JpaRepository<ActiveAccountToken, Long> {
    Optional<ActiveAccountToken> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ActiveAccountToken WHERE token = :token")
    void deleteByCode(@Param("token") String token);
}
