package com.coresaken.mcserverlist.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerRoleDto {
    List<RoleDto> roles;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDto{
        UserDto user;
        String role;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class UserDto{
            String login;
        }
    }
}
