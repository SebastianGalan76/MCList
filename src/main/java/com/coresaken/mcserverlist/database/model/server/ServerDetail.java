package com.coresaken.mcserverlist.database.model.server;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "TEXT")
    String motdHtml;

    @Column(columnDefinition = "TEXT")
    String motdClean;

    @Column(columnDefinition = "TEXT")
    String icon;
}
