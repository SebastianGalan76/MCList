package com.coresaken.mcserverlist.database.model.server;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mode {
    @Id
    Long id;

    String name;
}
