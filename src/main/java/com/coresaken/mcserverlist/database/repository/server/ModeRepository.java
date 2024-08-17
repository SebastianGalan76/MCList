package com.coresaken.mcserverlist.database.repository.server;

import com.coresaken.mcserverlist.database.model.server.Mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeRepository extends JpaRepository<Mode, Long> {
}
