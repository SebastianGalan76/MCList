package com.coresaken.mcserverlist.database.model.server.ratings;

import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    RatingCategory category;

    @ManyToOne
    @JoinColumn(name = "server_id")
    @JsonIgnore
    Server server;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    byte rate;
}
