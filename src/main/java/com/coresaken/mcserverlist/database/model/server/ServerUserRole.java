package com.coresaken.mcserverlist.database.model.server;

import com.coresaken.mcserverlist.database.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "server_user_role")
public class ServerUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "server_id")
    @JsonIgnore
    Server server;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    Role role;

    @Getter
    public enum Role {
        HELPER(500),
        MODERATOR(750),
        ADMINISTRATOR(1000),
        OWNER(5000),
        ;

        public final int value;

        Role(int value) {
            this.value = value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerUserRole cur = (ServerUserRole) o;
        return Objects.equals(id, cur.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
