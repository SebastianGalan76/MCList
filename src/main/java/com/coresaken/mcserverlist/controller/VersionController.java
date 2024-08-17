package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.database.model.server.Version;
import com.coresaken.mcserverlist.service.server.VersionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
public class VersionController {
    final VersionService versionService;

    @GetMapping("/version/listAll")
    public List<Version> getVersions(){
        return versionService.getVersionList();
    }
}
