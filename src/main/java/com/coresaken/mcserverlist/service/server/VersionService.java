package com.coresaken.mcserverlist.service.server;

import com.coresaken.mcserverlist.database.model.server.Version;
import com.coresaken.mcserverlist.database.repository.server.VersionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
@RequiredArgsConstructor
@Order(2)
public class VersionService implements CommandLineRunner  {
    final VersionRepository versionRepository;

    List<Version> versionList;

    @Override
    public void run(String... args) throws Exception {
        versionList = versionRepository.findAll();
    }
}
