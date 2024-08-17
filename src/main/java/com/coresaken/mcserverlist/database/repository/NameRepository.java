package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.server.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {
}