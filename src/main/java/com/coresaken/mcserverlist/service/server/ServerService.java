package com.coresaken.mcserverlist.service.server;

import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.ServerUserRole;
import com.coresaken.mcserverlist.database.repository.ServerRepository;
import com.coresaken.mcserverlist.database.repository.SubServerRepository;
import com.coresaken.mcserverlist.service.ServerStatusService;
import com.coresaken.mcserverlist.service.UserService;
import com.coresaken.mcserverlist.util.PermissionChecker;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerService {
    final UserService userService;
    final ServerStatusService serverStatusService;

    final ServerRepository serverRepository;
    final SubServerRepository subServerRepository;

    public Page<Server> getServers(int page){
        Pageable pageable = PageRequest.of(page - 1, 30);
        return serverRepository.findAllOrderByVotesAndId(pageable);
    }

    @Nullable
    public Server getServerById(Long serverId) {
        return serverRepository.findById(serverId).orElse(null);
    }

    @Nullable
    public Server getServerByIp(String serverIp) {
        return serverRepository.findByIp(serverIp).orElse(null);
    }

    @Nullable
    public String getRandomServerIp() {
        Server server = serverRepository.findRandomServer().orElse(null);
        return server == null ? null : server.getIp();
    }

    public void save(Server server){
        serverRepository.save(server);
    }

    public Response delete(Server server){
        if(!PermissionChecker.hasPermissionForServer(userService.getLoggedUser(), server, ServerUserRole.Role.OWNER)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić!").build();
        }

        serverRepository.delete(server);
        return Response.builder().status(HttpStatus.OK).build();
    }
}
