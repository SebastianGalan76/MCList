package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.server.ServerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerDetailRepository extends JpaRepository<ServerDetail, Long> {
}
