package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.ratings.PlayerRating;
import com.coresaken.mcserverlist.database.model.server.ratings.RatingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRatingRepository extends JpaRepository<PlayerRating, Long> {
    List<PlayerRating> findByServer(Server server);
    Optional<PlayerRating> findByUserAndServerAndCategory(User user, Server server, RatingCategory category);
}
