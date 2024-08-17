package com.coresaken.mcserverlist.database.model.server.staff;

import com.coresaken.mcserverlist.database.model.server.Server;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "staff_rank")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int index;

    String name;
    String color;

    @OneToMany(mappedBy = "rank", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("index ASC")
    List<Player> players;

    @ManyToOne
    @JoinColumn(name = "server_id")
    @JsonIgnore
    Server server;
}
