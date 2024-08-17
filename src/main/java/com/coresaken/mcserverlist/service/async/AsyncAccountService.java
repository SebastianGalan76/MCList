package com.coresaken.mcserverlist.service.async;

import com.coresaken.mcserverlist.database.model.ActiveAccountToken;
import com.coresaken.mcserverlist.database.repository.ActiveAccountTokenRepository;
import com.coresaken.mcserverlist.service.EmailSenderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncAccountService {
    private final ActiveAccountTokenRepository activeAccountTokenRepository;
    private final EmailSenderService emailSenderService;

    @Async
    public void processAccountActivation(Long userId, String email, String activeAccountToken) {
        try {
            activeAccountTokenRepository.save(new ActiveAccountToken(userId, activeAccountToken));
            emailSenderService.sendActiveAccountEmail(email, activeAccountToken);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
