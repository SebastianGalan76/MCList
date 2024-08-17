package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.service.UserService;
import com.coresaken.mcserverlist.service.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class PromoteServerController {
    final ServerService serverService;
    final UserService userService;

    @RequestMapping("/server/{ip}/promote")
    public String getServerPromotePage(@PathVariable("ip") String ip, Model model){
        Server server = serverService.getServerByIp(ip);

        if(server==null){
            return "error/404";
        }

        model.addAttribute("user", userService.getLoggedUser());
        model.addAttribute("server", server);
        return "subPage/promotionPoints";
    }
}
