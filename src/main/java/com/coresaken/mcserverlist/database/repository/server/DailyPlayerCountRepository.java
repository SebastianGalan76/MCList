package com.coresaken.mcserverlist.database.repository.server;

import com.coresaken.mcserverlist.database.model.server.DailyPlayerCount;
import com.coresaken.mcserverlist.database.model.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyPlayerCountRepository extends JpaRepository<DailyPlayerCount, Long> {
    @Query("SELECT d FROM DailyPlayerCount d WHERE DATE(d.time) = :date AND d.server = :server")
    List<DailyPlayerCount> findByDate(@Param("date") LocalDate date, @Param("server") Server server);
}
