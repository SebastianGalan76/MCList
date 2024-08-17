package com.coresaken.mcserverlist.database.model.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hourly_player_count")
public class HourlyPlayerCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime time;
    private int playerCount;

    @ManyToOne
    @JoinColumn(name = "server_id")
    @JsonIgnore
    private Server server;
}