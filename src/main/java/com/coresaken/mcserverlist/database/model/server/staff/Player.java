package com.coresaken.mcserverlist.database.model.server.staff;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int index;

    String nick;

    String discord;
    String instagram;
    String tiktok;
    String youtube;

    @ManyToOne
    @JoinColumn(name = "rank_id")
    @JsonIgnore
    @ToString.Exclude
    Rank rank;
}
