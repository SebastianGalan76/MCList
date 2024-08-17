package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.data.dto.StringDto;
import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.service.UserService;
import com.coresaken.mcserverlist.service.server.ReportServerService;
import com.coresaken.mcserverlist.service.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ReportServerController {
    final ReportServerService reportServerService;
    final ServerService serverService;
    final UserService userService;

    @RequestMapping("/server/{id}/report")
    public String getServerReportPage(@PathVariable("id") Long id, Model model){
        Server server = serverService.getServerById(id);

        if(server==null){
            return "error/404";
        }

        model.addAttribute("user", userService.getLoggedUser());
        model.addAttribute("server", server);
        return "subPage/report";
    }

    @ResponseBody
    @PostMapping("/server/{id}/report/send")
    public Response reportServer(@PathVariable("id") Long id, @RequestBody StringDto stringDto){
        return reportServerService.reportServer(id, stringDto.getText());
    }
}
