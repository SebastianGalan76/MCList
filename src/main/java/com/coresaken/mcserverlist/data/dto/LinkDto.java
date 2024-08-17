package com.coresaken.mcserverlist.data.dto;

import com.coresaken.mcserverlist.database.model.server.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {
    List<Link> links;
}