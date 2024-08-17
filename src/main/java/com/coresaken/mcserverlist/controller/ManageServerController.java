package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.data.dto.*;
import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.ServerUserRole;
import com.coresaken.mcserverlist.service.UserService;
import com.coresaken.mcserverlist.service.server.ManageServerService;
import com.coresaken.mcserverlist.service.server.ServerService;
import com.coresaken.mcserverlist.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ManageServerController {
    final ManageServerService manageServerService;
    final ServerService serverService;

    final UserService userService;

    @RequestMapping("/server/{id}/manage")
    public String getManagePage(@PathVariable("id") Long serverId, Model model){
        Server server = serverService.getServerById(serverId);
        User user = userService.getLoggedUser();

        if(server==null){
            return "error/404";
        }
        if(!PermissionChecker.hasPermissionForServer(user, server, ServerUserRole.Role.HELPER)){
            return "error/403";
        }

        model.addAttribute("user", user);
        model.addAttribute("server", server);
        model.addAttribute("role", PermissionChecker.getRoleForServer(user, server));

        return "manage/manageHome";
    }

    @RequestMapping("/server/{id}/manage/info")
    public String getManageInfoPage(@PathVariable("id") Long serverId, Model model){
        Server server = serverService.getServerById(serverId);
        User user = userService.getLoggedUser();

        if(server==null){
            return "error/404";
        }
        if(!PermissionChecker.hasPermissionForServer(user, server, ServerUserRole.Role.MODERATOR)){
            return "error/403";
        }

        model.addAttribute("user", user);
        model.addAttribute("server", server);
        model.addAttribute("role", PermissionChecker.getRoleForServer(user, server));

        return "manage/manageInfo";
    }

    @ResponseBody
    @PostMapping("/server/{id}/manage/info/save")
    public Response saveServerInfo(@PathVariable("id") Long serverId, @RequestBody BasicServerDto serverDto){
        Server server = serverService.getServerById(serverId);

        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #4121. Możesz zgłosić go do Administracji strony.").build();
        }
        if(!PermissionChecker.hasPermissionForServer(userService.getLoggedUser(), server, ServerUserRole.Role.MODERATOR)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić!").build();
        }

        return manageServerService.saveServerInfo(server, serverDto);
    }

    @RequestMapping("/server/{id}/manage/staff")
    public String getManageStaffPage(@PathVariable("id") Long serverId, Model model){
        Server server = serverService.getServerById(serverId);
        User user = userService.getLoggedUser();

        if(server==null){
            return "error/404";
        }
        if(!PermissionChecker.hasPermissionForServer(user, server, ServerUserRole.Role.HELPER)){
            return "error/403";
        }

        model.addAttribute("user", user);
        model.addAttribute("server", server);
        model.addAttribute("role", PermissionChecker.getRoleForServer(user, server));

        return "manage/manageStaff";
    }

    @ResponseBody
    @PostMapping("/server/{id}/manage/staff/save")
    public Response saveServerStaff(@PathVariable("id") Long serverId, @RequestBody StaffDto staffDto){
        Server server = serverService.getServerById(serverId);

        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #4121. Możesz zgłosić go do Administracji strony.").build();
        }
        if(!PermissionChecker.hasPermissionForServer(userService.getLoggedUser(), server, ServerUserRole.Role.HELPER)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić!").build();
        }

        return manageServerService.saveServerStaff(server, staffDto);
    }

    @RequestMapping("/server/{id}/manage/description")
    public String getManageDescriptionPage(@PathVariable("id") Long serverId, Model model){
        Server server = serverService.getServerById(serverId);
        User user = userService.getLoggedUser();

        if(server==null){
            return "error/404";
        }
        if(!PermissionChecker.hasPermissionForServer(user, server, ServerUserRole.Role.HELPER)){
            return "error/403";
        }

        model.addAttribute("user", user);
        model.addAttribute("server", server);
        model.addAttribute("role", PermissionChecker.getRoleForServer(user, server));

        return "manage/manageDescription";
    }

    @ResponseBody
    @PostMapping("/server/{id}/manage/description/save")
    public Response saveServerDescription(@PathVariable("id") Long serverId, @RequestBody StringDto stringDto){
        Server server = serverService.getServerById(serverId);

        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #4121. Możesz zgłosić go do Administracji strony.").build();
        }
        if(!PermissionChecker.hasPermissionForServer(userService.getLoggedUser(), server, ServerUserRole.Role.HELPER)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić!").build();
        }

        return manageServerService.saveServerDescription(server, stringDto);
    }

    @RequestMapping("/server/{id}/manage/link")
    public String getManageLinkPage(@PathVariable("id") Long serverId, Model model){
        Server server = serverService.getServerById(serverId);
        User user = userService.getLoggedUser();

        if(server==null){
            return "error/404";
        }
        if(!PermissionChecker.hasPermissionForServer(user, server, ServerUserRole.Role.MODERATOR)){
            return "error/403";
        }

        model.addAttribute("user", user);
        model.addAttribute("server", server);
        model.addAttribute("role", PermissionChecker.getRoleForServer(user, server));

        return "manage/manageLink";
    }

    @ResponseBody
    @PostMapping("/server/{id}/manage/link/save")
    public Response saveServerLinks(@PathVariable("id") Long serverId, @RequestBody LinkDto linkDto){
        Server server = serverService.getServerById(serverId);

        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #4121. Możesz zgłosić go do Administracji strony.").build();
        }
        if(!PermissionChecker.hasPermissionForServer(userService.getLoggedUser(), server, ServerUserRole.Role.MODERATOR)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić!").build();
        }

        return manageServerService.saveServerLinks(server, linkDto.getLinks());
    }

    @RequestMapping("/server/{id}/manage/banner")
    public String getManageBannerPage(@PathVariable("id") Long serverId, Model model){
        Server server = serverService.getServerById(serverId);
        User user = userService.getLoggedUser();

        if(server==null){
            return "error/404";
        }
        if(!PermissionChecker.hasPermissionForServer(user, server, ServerUserRole.Role.MODERATOR)){
            return "error/403";
        }

        model.addAttribute("user", user);
        model.addAttribute("server", server);
        model.addAttribute("role", PermissionChecker.getRoleForServer(user, server));

        return "manage/manageBanner";
    }

    @ResponseBody
    @PostMapping("/server/{id}/manage/banner/save")
    public Response saveServerBanner(@PathVariable("id") Long serverId, @Param("file") MultipartFile file, @Param("url") String url){
        Server server = serverService.getServerById(serverId);

        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #4121. Możesz zgłosić go do Administracji strony.").build();
        }
        if(!PermissionChecker.hasPermissionForServer(userService.getLoggedUser(), server, ServerUserRole.Role.MODERATOR)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić!").build();
        }

        return manageServerService.saveServerBanner(server, file, url);
    }

    @RequestMapping("/server/{id}/manage/role")
    public String getManageRolePage(@PathVariable("id") Long serverId, Model model){
        Server server = serverService.getServerById(serverId);
        User user = userService.getLoggedUser();

        if(server==null){
            return "error/404";
        }
        if(!PermissionChecker.hasPermissionForServer(user, server, ServerUserRole.Role.ADMINISTRATOR)){
            return "error/403";
        }

        model.addAttribute("user", user);
        model.addAttribute("server", server);
        model.addAttribute("role", PermissionChecker.getRoleForServer(user, server));

        return "manage/manageRole";
    }

    @ResponseBody
    @PostMapping("/server/{id}/manage/role/save")
    public Response saveServerRoles(@PathVariable("id") Long serverId, @RequestBody ServerRoleDto serverRoleDto){
        Server server = serverService.getServerById(serverId);

        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #4262. Możesz zgłosić go do Administracji strony.").build();
        }
        if(!PermissionChecker.hasPermissionForServer(userService.getLoggedUser(), server, ServerUserRole.Role.ADMINISTRATOR)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić!").build();
        }

        return manageServerService.saveServerRoles(server, serverRoleDto);
    }

    @RequestMapping("/server/{id}/manage/subserver")
    public String getManageSubServerPage(@PathVariable("id") Long serverId, Model model){
        Server server = serverService.getServerById(serverId);
        User user = userService.getLoggedUser();

        if(server==null){
            return "error/404";
        }
        if(!PermissionChecker.hasPermissionForServer(user, server, ServerUserRole.Role.MODERATOR)){
            return "error/403";
        }

        model.addAttribute("user", user);
        model.addAttribute("server", server);
        model.addAttribute("role", PermissionChecker.getRoleForServer(user, server));

        return "manage/manageSubserver";
    }

    @ResponseBody
    @PostMapping("/server/{id}/manage/subserver/save")
    public Response saveSubServers(@PathVariable("id") Long serverId, @RequestBody ListDto<SubServerDto> subServersDto){
        Server server = serverService.getServerById(serverId);

        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #4262. Możesz zgłosić go do Administracji strony.").build();
        }
        if(!PermissionChecker.hasPermissionForServer(userService.getLoggedUser(), server, ServerUserRole.Role.MODERATOR)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić!").build();
        }

        return manageServerService.saveSubServers(server, subServersDto);
    }

    @RequestMapping("/server/{id}/manage/delete")
    public String getManageDeletePage(@PathVariable("id") Long serverId, Model model){
        Server server = serverService.getServerById(serverId);
        User user = userService.getLoggedUser();

        if(server==null){
            return "error/404";
        }
        if(!PermissionChecker.hasPermissionForServer(user, server, ServerUserRole.Role.OWNER)){
            return "error/403";
        }

        model.addAttribute("user", user);
        model.addAttribute("server", server);
        model.addAttribute("role", PermissionChecker.getRoleForServer(user, server));

        return "manage/manageDelete";
    }

}
