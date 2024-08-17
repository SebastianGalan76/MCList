package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.data.dto.VoteDto;
import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.server.Vote;
import com.coresaken.mcserverlist.service.server.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VoteController {
    final VoteService voteService;

    @PostMapping("/vote")
    public Response vote(@RequestBody VoteDto voteDto, HttpServletRequest request){
        return voteService.vote(voteDto, request);
    }

    @GetMapping("/api/vote/check/{server_id}/{player_nick}")
    public ResponseEntity<?> checkVoteForPlayer(@PathVariable("server_id") Long serverId, @PathVariable("player_nick") String playerNick){
        return voteService.check(serverId, playerNick);
    }

    @PostMapping("/api/vote/confirm/{server_id}/{vote_id}")
    public ResponseEntity<?> checkVoteForPlayer(@PathVariable("server_id") Long serverId, @PathVariable("vote_id") Long voteId){
        return voteService.confirm(voteId, serverId);
    }
}
