package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.data.dto.ServerStatusDto;
import com.coresaken.mcserverlist.service.ServerStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServerStatusController {
    final ServerStatusService serverStatusService;

    @GetMapping("/server/status/{address}")
    public ServerStatusDto getServerStatus(@PathVariable String address) {
        return serverStatusService.getServerStatus(address);
    }
}
