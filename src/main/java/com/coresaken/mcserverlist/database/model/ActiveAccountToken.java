package com.coresaken.mcserverlist.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "active_account_token")
public class ActiveAccountToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long userId;

    @Column(length = 30)
    String token;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime createdAt;

    public ActiveAccountToken(Long userId, String token){
        this.userId = userId;
        this.token = token;

        createdAt = LocalDateTime.now();
    }
}
