package com.coresaken.mcserverlist.database.model.server;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockedServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    Server server;

    @Column(unique = true)
    String ip;

    @Column(name = "expire_at", columnDefinition = "TIMESTAMP")
    LocalDateTime expireAt;
}
