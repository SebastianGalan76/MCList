package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.data.dto.SearchServerDto;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.service.server.SearchServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SearchServerController {
    final SearchServerService searchServerService;

    @PostMapping("/server/search/{page}")
    public Page<Server> searchServer(@RequestBody SearchServerDto searchServerDto, @PathVariable("page") int page){
        Pageable pageable = PageRequest.of(page - 1, 30);

        return searchServerService.searchServer(searchServerDto, pageable);
    }
}
