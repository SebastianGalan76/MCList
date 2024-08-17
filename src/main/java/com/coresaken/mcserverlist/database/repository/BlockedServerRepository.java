package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.server.BlockedServer;
import com.coresaken.mcserverlist.database.model.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedServerRepository extends JpaRepository<BlockedServer, Long> {
    @Query("SELECT s FROM BlockedServer s WHERE LOWER(s.ip) = LOWER(:ip)")
    Optional<BlockedServer> findByIp(@Param("ip") String ip);
}
