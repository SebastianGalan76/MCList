package com.coresaken.mcserverlist.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ServerStatusDto(boolean online, Player players, Motd motd, String icon) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Player(int online, int max) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Motd(String clean, String html) {
    }
}
