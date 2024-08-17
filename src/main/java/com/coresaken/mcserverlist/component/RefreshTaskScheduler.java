package com.coresaken.mcserverlist.component;

import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.repository.ServerRepository;
import com.coresaken.mcserverlist.service.PlayerStatsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class RefreshTaskScheduler {
    final ServerRepository serverRepository;
    final RefreshTaskQueue taskQueue;
    final PlayerStatsService playerStatsService;
    final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void startProcessing() {
        startProcessingThread();
    }

    private void startProcessingThread() {
        executorService.submit(() -> {
            while (true) {
                try {
                    Server server = taskQueue.pollTask();
                    if (server == null) {
                        continue;
                    }

                    LocalDateTime now = LocalDateTime.now();
                    if (server.getNextRefreshAt().isAfter(now.minusMinutes(1))) {
                        playerStatsService.refreshServer(server);
                    } else {
                        LocalDateTime time = server.getNextRefreshAt();
                        long minutesDifference = java.time.Duration.between(time, now).toMinutes();
                        long increments = (minutesDifference / 30) + 1;

                        server.setNextRefreshAt(time.plusMinutes(increments * 30));
                        serverRepository.save(server);
                    }

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println("Wątek został przerwany: " + e.getMessage());
                    Thread.currentThread().interrupt();
                    restartProcessingThread();
                    break;
                } catch (Exception e) {
                    System.err.println("Błąd w wątku: " + e.getMessage());
                }
            }
        });
    }

    private void restartProcessingThread() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Ponowne uruchamianie wątku przetwarzania zadań...");
        startProcessingThread();
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Server> serversToRefresh = serverRepository.findServersToRefresh(now);
        for (Server server : serversToRefresh) {
            taskQueue.addTask(server);
        }
    }
}