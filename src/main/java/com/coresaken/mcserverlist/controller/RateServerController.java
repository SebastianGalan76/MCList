package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.ratings.PlayerRating;
import com.coresaken.mcserverlist.service.server.RateServerService;
import com.coresaken.mcserverlist.service.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RateServerController {
    final RateServerService rateServerService;
    final ServerService serverService;

    @PostMapping("/server/{ip}/rate/save")
    public Response getServerRatePage(@PathVariable("ip") String ip, @RequestBody List<PlayerRating> playerRatings){
        Server server = serverService.getServerByIp(ip);

        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #8741. Możesz zgłosić go do Administracji strony.").build();
        }

        return rateServerService.rateServer(server, playerRatings);
    }
}
