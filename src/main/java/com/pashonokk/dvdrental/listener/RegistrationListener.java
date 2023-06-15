package com.pashonokk.dvdrental.listener;

import com.pashonokk.dvdrental.event.UserRegistrationCompletedEvent;
import com.pashonokk.dvdrental.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationListener {
    private final NotificationService notificationService;

    @SneakyThrows
    @EventListener
    public void sendNotification(UserRegistrationCompletedEvent event) {
        notificationService.send(event.getEmailDto());
    }
}
