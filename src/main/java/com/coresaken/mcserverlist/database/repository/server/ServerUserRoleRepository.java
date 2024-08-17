package com.coresaken.mcserverlist.database.repository.server;

import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.model.server.ServerUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerUserRoleRepository extends JpaRepository<ServerUserRole, Long> {
    List<ServerUserRole> findByUser(User user);
}
