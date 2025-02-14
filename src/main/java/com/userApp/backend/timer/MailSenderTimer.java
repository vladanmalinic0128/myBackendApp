package com.userApp.backend.timer;

import com.userApp.backend.services.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class MailSenderTimer {
    private final SubscriptionService subscriptionService;

    @Scheduled(fixedRate = 24*60*60*1000)//every one day
    public void runTask() {
        subscriptionService.sendEmails();
    }
}