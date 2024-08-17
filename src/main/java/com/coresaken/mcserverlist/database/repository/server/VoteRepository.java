package com.coresaken.mcserverlist.database.repository.server;

import com.coresaken.mcserverlist.database.model.server.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT v FROM Vote v WHERE (v.ip = :ip OR (:nick IS NOT NULL AND :nick <> '' AND v.nick = :nick)) AND v.votedAt = :votedAt AND v.server.id = :serverId AND v.nick IS NOT NULL")
    List<Vote> findByIpOrNickAndDateAndServerId(@Param("ip") String ip, @Param("nick") String nick, @Param("votedAt") LocalDate votedAt, @Param("serverId") Long serverId);

    @Query(value = "SELECT * FROM Vote v WHERE v.nick = :nick AND v.server_id = :serverId AND v.received = false LIMIT 1", nativeQuery = true)
    Vote findFirstByNickAndServerIdAndReceivedFalse(
            @Param("nick") String nick,
            @Param("serverId") Long serverId
    );
}
