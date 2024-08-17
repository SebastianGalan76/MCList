package com.coresaken.mcserverlist.service.server;

import com.coresaken.mcserverlist.data.response.VoteResponse;
import com.coresaken.mcserverlist.data.dto.VoteDto;
import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.Vote;
import com.coresaken.mcserverlist.database.repository.ServerRepository;
import com.coresaken.mcserverlist.database.repository.server.VoteRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class VoteService {
    final VoteRepository voteRepository;
    final ServerRepository serverRepository;

    public Response vote(VoteDto voteDto, HttpServletRequest request){
        LocalDate today = LocalDate.now();
        String ip = request.getRemoteAddr();

        Optional<Server> server = serverRepository.findById(voteDto.getServerId());
        if(server.isEmpty()){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd. Spróbuj ponownie później").build();
        }

        List<Vote> votes = voteRepository.findByIpOrNickAndDateAndServerId(ip, voteDto.getNick(), today, voteDto.getServerId());
        if(votes != null && !votes.isEmpty()){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Głosowałeś/aś już dzisiaj na serwer").build();
        }

        Vote vote = new Vote();
        vote.setVotedAt(today);
        vote.setReceived(false);
        vote.setServer(server.get());

        vote.setIp(ip);
        vote.setNick(voteDto.getNick());

        voteRepository.save(vote);
        return Response.builder().status(HttpStatus.OK).message("Głos został oddany").build();
    }

    public ResponseEntity<VoteResponse> check(Long serverId, String playerNick) {
        Optional<Server> server = serverRepository.findById(serverId);
        if(server.isEmpty()){
            return new ResponseEntity<>(new VoteResponse(null, null, 1, "No server with the specified ID") , HttpStatus.BAD_REQUEST);
        }

        Vote vote = voteRepository.findFirstByNickAndServerIdAndReceivedFalse(playerNick, serverId);
        if(vote!=null){
            return new ResponseEntity<>(new VoteResponse(vote.getId(), vote.getVotedAt(), 0, null) , HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new VoteResponse(null, null, 2, "This player has not voted or has already received his reward") , HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> confirm(Long voteId, Long serverId) {
        Vote vote = voteRepository.findById(voteId).orElse(null);
        if(vote!=null){
            if(vote.getServer().getId().equals(serverId)){
                vote.setReceived(true);
                voteRepository.save(vote);
                return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
            }

            return new ResponseEntity<>("No vote with the specified server's ID" , HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>("No vote with the specified ID" , HttpStatus.BAD_REQUEST);
        }
    }
}
