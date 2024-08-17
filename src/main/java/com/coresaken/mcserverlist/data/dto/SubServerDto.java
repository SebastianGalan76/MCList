package com.coresaken.mcserverlist.data.dto;

import com.coresaken.mcserverlist.database.model.server.Mode;
import com.coresaken.mcserverlist.database.model.server.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubServerDto {
    Long id;
    int index;
    String name;
    String color;
    List<Version> versions;
    Mode mode;
}
