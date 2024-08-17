package com.coresaken.mcserverlist.util;

import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.ServerUserRole;

public class PermissionChecker {

    public static boolean hasPermissionForServer(User user, Server server, ServerUserRole.Role minRole){
        if(user == null || server == null){
            return false;
        }
        if(user.getRole() == User.Role.ADMIN){
            return true;
        }

        return !server.getServerUserRoles().stream().filter(role -> role.getRole().value >= minRole.value && role.getUser().equals(user)).toList().isEmpty();
    }

    public static ServerUserRole.Role getRoleForServer(User user, Server server){
        if(user == null || server == null){
            return null;
        }
        if(user.getRole() == User.Role.ADMIN){
            return ServerUserRole.Role.OWNER;
        }

        ServerUserRole sur = server.getServerUserRoles().stream().filter(role -> role.getUser().equals(user)).findFirst().orElse(null);
        if(sur==null){
            return null;
        }
        return sur.getRole();
    }
}
