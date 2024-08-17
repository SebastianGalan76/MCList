package com.coresaken.mcserverlist.service;

import com.coresaken.mcserverlist.data.dto.ServerStatusDto;
import com.coresaken.mcserverlist.database.model.server.DailyPlayerCount;
import com.coresaken.mcserverlist.database.model.server.HourlyPlayerCount;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.model.server.ServerDetail;
import com.coresaken.mcserverlist.database.repository.HourlyPlayerCountRepository;
import com.coresaken.mcserverlist.database.repository.ServerDetailRepository;
import com.coresaken.mcserverlist.database.repository.ServerRepository;
import com.coresaken.mcserverlist.database.repository.server.DailyPlayerCountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PlayerStatsService {
    static final int REFRESH_INTERVAL_MINUTE = 30;

    final ServerRepository serverRepository;
    final ServerDetailRepository serverDetailRepository;
    final HourlyPlayerCountRepository hourlyPlayerCountRepository;
    final DailyPlayerCountRepository dailyPlayerCountRepository;

    final ServerStatusService serverStatusService;

    @Transactional
    private void savePlayerStatistic(Server server, int playerCount) {
        LocalDateTime now = LocalDateTime.now();
        HourlyPlayerCount hourlyPlayerCount = new HourlyPlayerCount();
        hourlyPlayerCount.setTime(now);
        hourlyPlayerCount.setPlayerCount(playerCount);
        hourlyPlayerCount.setServer(server);
        List<HourlyPlayerCount> existingCounts = hourlyPlayerCountRepository.findByServerOrderByTimeDesc(server);
        if (existingCounts.size() >= 336) {
            hourlyPlayerCountRepository.delete(existingCounts.get(existingCounts.size() - 1));
        }
        List<DailyPlayerCount> dailyPlayerCountList = dailyPlayerCountRepository.findByDate(LocalDate.now(), server);
        if(!dailyPlayerCountList.isEmpty()){
            DailyPlayerCount dailyPlayerCount = dailyPlayerCountList.get(0);
            if(dailyPlayerCount.getPlayerCount() < playerCount){
                dailyPlayerCount.setPlayerCount(playerCount);
                dailyPlayerCount.setTime(now);
                dailyPlayerCountRepository.save(dailyPlayerCount);
            }
        }
        else{
            DailyPlayerCount newDailyPlayerCount = new DailyPlayerCount();
            newDailyPlayerCount.setTime(now);
            newDailyPlayerCount.setPlayerCount(playerCount);
            newDailyPlayerCount.setServer(server);
            dailyPlayerCountRepository.save(newDailyPlayerCount);
        }
        hourlyPlayerCountRepository.save(hourlyPlayerCount);
    }

    public void refreshServer(Server server) {
        if(server==null){
            return;
        }

        ServerStatusDto serverResponse = serverStatusService.getServerStatus(server.getIp(), server.getPort());
        boolean online = serverResponse.online();
        server.setOnline(online);
        if(online){
            server.setOnlinePlayers(serverResponse.players().online());

            ServerDetail serverDetail = server.getDetail();
            serverDetail.setIcon(serverResponse.icon());
            serverDetail.setMotdHtml(serverResponse.motd().html());
            serverDetail.setMotdClean(serverResponse.motd().clean());
            server.setDetail(serverDetail);

            savePlayerStatistic(server, serverResponse.players().online());
            serverDetailRepository.save(serverDetail);
        }

        server.setNextRefreshAt(LocalDateTime.now().plusMinutes(REFRESH_INTERVAL_MINUTE));
        serverRepository.save(server);
    }
}
