package com.coresaken.mcserverlist.database.model.server;

import com.coresaken.mcserverlist.database.model.server.staff.Rank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "server")
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String ip;
    int port;

    @ManyToOne
    @JoinColumn(name = "detail_id")
    ServerDetail detail;

    @ManyToOne
    @JoinColumn(name = "name_id")
    Name name;

    @Column(columnDefinition = "TEXT")
    String description;

    String banner;

    boolean premium = false;
    boolean mods = false;

    @ManyToOne
    @JoinColumn(name = "mode_id")
    Mode mode;

    boolean online;

    int onlinePlayers;

    LocalDateTime nextRefreshAt;
    LocalDateTime createdAt;

    int promotionPoints;

    @OneToMany(
            mappedBy = "server",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("index ASC")
    List<SubServer> subServers;

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("index ASC")
    List<Rank> staff;

    @ManyToMany
    @JoinTable(
            name = "server_version",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "version_id")
    )
    List<Version> versions = new ArrayList<>();

    @OneToMany(
            mappedBy = "server",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<Vote> votes = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("index ASC")
    List<Link> links = new ArrayList<>();

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    List<HourlyPlayerCount> hourlyPlayerCounts;

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    List<DailyPlayerCount> dailyPlayerCounts;

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ServerUserRole> serverUserRoles = new HashSet<>();
}
