package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.database.model.server.Mode;
import com.coresaken.mcserverlist.service.server.ModeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
public class ModeController {
    final ModeService modeService;

    @GetMapping("/mode/listAll")
    public List<Mode> getModes(){
        return modeService.getModeList();
    }
}
