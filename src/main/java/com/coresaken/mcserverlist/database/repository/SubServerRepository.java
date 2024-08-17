package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.server.Mode;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.SubServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubServerRepository extends JpaRepository<SubServer, Long> {
    @Query("SELECT DISTINCT s.server FROM SubServer s " +
            "JOIN s.versions v " +
            "WHERE (:mode IS NULL OR s.mode = :mode) " +
            "AND (:versionId = 0 " +
            "    OR (v.id <= :versionId AND EXISTS (SELECT 1 FROM s.versions v2 WHERE v2.id >= :versionId)) " +
            "    OR (SIZE(s.versions) = 1 AND v.id = :versionId))")
    List<Server> findServersByModeAndVersion(@Param("mode") Mode mode, @Param("versionId") Long versionId);
}
