package com.coresaken.mcserverlist.database.model.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "server_id")
    @JsonIgnore
    Server server;

    @Column(name = "voted_at", columnDefinition = "DATE")
    LocalDate votedAt;

    boolean received;

    //Voting person
    String ip;
    String nick;
}
