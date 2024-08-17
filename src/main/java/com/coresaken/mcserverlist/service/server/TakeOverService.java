package com.coresaken.mcserverlist.service.server;

import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.ServerUserRole;
import com.coresaken.mcserverlist.service.ServerStatusService;
import com.coresaken.mcserverlist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TakeOverService {
    final UserService userService;
    final ServerService serverService;
    final ServerStatusService serverStatusService;

    public Response takeOver(Long serverId){
        User user = userService.getLoggedUser();
        if(user == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Twoja sesja wygasła. Zaloguj się ponownie").build();
        }

        Server server = serverService.getServerById(serverId);
        if(server == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #9928").build();
        }
        for (ServerUserRole sur:server.getServerUserRoles()){
            if(sur.getRole()== ServerUserRole.Role.OWNER){
                return Response.builder().status(HttpStatus.BAD_REQUEST).message("Serwer posiada już właściciela. Jeśli ktoś przejął Twój serwer, skontaktuj się z nami").build();
            }
        }

        String motd = serverStatusService.getServerStatus(server.getIp(), server.getPort()).motd().clean();

        if(motd.contains(user.getUuid())){
            ServerUserRole sur = new ServerUserRole();
            sur.setRole(ServerUserRole.Role.OWNER);
            sur.setUser(user);
            sur.setServer(server);

            server.getServerUserRoles().add(sur);
            serverService.save(server);
            return Response.builder().status(HttpStatus.OK).message("Serwer został prawidłowo przejęty").build();
        }

        return Response.builder().status(HttpStatus.BAD_REQUEST).message("Błędna weryfikacja. Jesteś pewien, że zresetowałeś serwer po zmianie MOTD serwera? Serwer musi być włączony. Obecny motd serwera: "+motd).build();
    }

}
