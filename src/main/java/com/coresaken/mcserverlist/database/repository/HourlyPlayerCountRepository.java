package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.server.HourlyPlayerCount;
import com.coresaken.mcserverlist.database.model.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HourlyPlayerCountRepository extends JpaRepository<HourlyPlayerCount, Long> {
    List<HourlyPlayerCount> findByServerOrderByTimeDesc(Server server);
}
