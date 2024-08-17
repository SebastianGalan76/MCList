package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.server.Mode;
import com.coresaken.mcserverlist.database.model.server.Server;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
    @Query("SELECT s FROM Server s WHERE LOWER(s.ip) = LOWER(:ip)")
    Optional<Server> findByIp(@Param("ip") String ip);

    @Query("SELECT s FROM Server s WHERE s.nextRefreshAt IS NOT NULL")
    List<Server> findAllServersWithNextRefreshAtNotNull();

    @Query("SELECT s FROM Server s LEFT JOIN s.votes v GROUP BY s ORDER BY s.promotionPoints DESC, COUNT(v) DESC, s.id DESC")
    Page<Server> findAllOrderByVotesAndId(Pageable pageable);

    @Query("SELECT s FROM Server s WHERE LOWER(s.ip) LIKE LOWER(CONCAT('%', :ip, '%'))")
    List<Server> searchByIp(@Param("ip") String ip);

    @Query("SELECT s FROM Server s JOIN s.detail d WHERE LOWER(d.motdClean) LIKE LOWER(CONCAT('%', :motdClean, '%'))")
    List<Server> searchByMotd(@Param("motdClean") String motdClean);

    @Query("SELECT DISTINCT s FROM Server s " +
            "LEFT JOIN s.versions v " +
            "WHERE (:mode IS NULL OR s.mode = :mode) " +
            "AND (:versionId = 0 " +
            "    OR (v.id <= :versionId AND EXISTS (SELECT 1 FROM s.versions v2 WHERE v2.id >= :versionId)) " +
            "    OR (SIZE(s.versions) = 1 AND v.id = :versionId))")
    List<Server> findServersByModeAndVersionRange(
            @Param("mode") Mode mode,
            @Param("versionId") Long versionId
    );


    @Query("SELECT s FROM Server s WHERE s.premium = true")
    List<Server> findAllPremiumServers();

    @Query("SELECT s FROM Server s WHERE s.mods = true")
    List<Server> findAllServersWithMods();

    @Modifying
    @Transactional
    @Query("UPDATE Server s SET s.promotionPoints = s.promotionPoints - 1 WHERE s.promotionPoints > 0")
    void decreasePromotionPoints();

    @Query("SELECT s FROM Server s WHERE s.nextRefreshAt <= :now")
    List<Server> findServersToRefresh(LocalDateTime now);

    @Query("SELECT s FROM Server s ORDER BY RANDOM() LIMIT 1")
    Optional<Server> findRandomServer();
}
