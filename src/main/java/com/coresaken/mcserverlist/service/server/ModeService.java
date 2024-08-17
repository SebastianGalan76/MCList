package com.coresaken.mcserverlist.service.server;

import com.coresaken.mcserverlist.database.model.server.Mode;
import com.coresaken.mcserverlist.database.repository.server.ModeRepository;
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
public class ModeService implements CommandLineRunner {
    final ModeRepository modeRepository;

    List<Mode> modeList;
    Mode networkMode;

    @Override
    public void run(String... args) throws Exception {
        modeList = modeRepository.findAll();

        networkMode = modeList.stream()
                .filter(mode -> mode.getId() == 1)
                .findFirst().orElse(null);
    }
}
