package com.coresaken.mcserverlist.service.server;

import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.ratings.PlayerRating;
import com.coresaken.mcserverlist.database.repository.PlayerRatingRepository;
import com.coresaken.mcserverlist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateServerService {
    final UserService userService;
    final PlayerRatingRepository playerRatingRepository;

    public Response rateServer(Server server, List<PlayerRating> playerRatings){
        User user = userService.getLoggedUser();

        if(user==null){
            return Response.builder().status(HttpStatus.UNAUTHORIZED).message("Twoja sesja wygasła. Zaloguj się ponownie, aby ocenić serwer!").build();
        }
        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd. Kod błędu #3251. Zgłoś go do Administratora strony!").build();
        }

        //Remove ratings if all ratings are equal to 1
        boolean antiKid = false;
        for(PlayerRating pr:playerRatings){
            if (pr.getRate() > 1) {
                antiKid = true;
                break;
            }
        }
        if(!antiKid){
            return Response.builder().status(HttpStatus.OK).build();
        }

        for(PlayerRating pr:playerRatings){
            if(pr.getRate() == 0){
                continue;
            }

            Optional<PlayerRating> existingRating = playerRatingRepository.findByUserAndServerAndCategory(user, server, pr.getCategory());
            if (existingRating.isPresent()) {
                PlayerRating playerRating = existingRating.get();
                playerRating.setRate(pr.getRate());
                playerRatingRepository.save(playerRating);
            } else {
                pr.setUser(user);
                pr.setServer(server);
                playerRatingRepository.save(pr);
            }
        }

        return Response.builder().status(HttpStatus.OK).build();
    }

}
