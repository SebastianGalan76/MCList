package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.service.UserService;
import com.coresaken.mcserverlist.service.server.ServerService;
import com.coresaken.mcserverlist.service.server.TakeOverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TakeOverController {
    final TakeOverService takeOverService;
    final ServerService serverService;
    final UserService userService;

    @RequestMapping("/server/{id}/take")
    public String getServerTakeOverPage(@PathVariable("id") Long id, Model model){
        Server server = serverService.getServerById(id);

        if(server==null){
            return "error/404";
        }

        model.addAttribute("user", userService.getLoggedUser());
        model.addAttribute("server", server);
        return "subPage/takeOver";
    }

    @ResponseBody
    @GetMapping("/take-over/{id}")
    public Response takeOverServer(@PathVariable("id") Long id){
        return takeOverService.takeOver(id);
    }
}
