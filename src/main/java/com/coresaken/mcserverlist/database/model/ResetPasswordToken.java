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
@Table(name = "reset_password_token")
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    User user;

    String token;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime expiredAt;

    public ResetPasswordToken(User user, String token){
        this.user = user;
        this.token = token;

        expiredAt = LocalDateTime.now().plusMinutes(10);
    }
}
