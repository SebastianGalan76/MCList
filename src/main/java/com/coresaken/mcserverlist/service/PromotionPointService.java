package com.coresaken.mcserverlist.service;

import com.coresaken.mcserverlist.data.payment.PromotionPoints;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromotionPointService {
    final ServerRepository serverRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void executeTask() {
        serverRepository.decreasePromotionPoints();
    }

    @Transactional
    public void addPromotionPoints(PromotionPoints promotionPoints) {
        Server server = serverRepository.findById(promotionPoints.getServerId()).orElse(null);
        if(server == null){
            return;
        }

        server.setPromotionPoints(server.getPromotionPoints() + promotionPoints.getPromotionPoints());
    }
}
