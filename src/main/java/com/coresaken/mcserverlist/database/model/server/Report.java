package com.coresaken.mcserverlist.database.model.server;

import com.coresaken.mcserverlist.database.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 512)
    String reason;

    @ManyToOne
    @JoinColumn(name = "server_id")
    Server server;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
