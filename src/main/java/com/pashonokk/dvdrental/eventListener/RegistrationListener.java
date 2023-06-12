package com.pashonokk.dvdrental.eventListener;

import com.pashonokk.dvdrental.event.UserRegistrationCompletedEvent;
import com.pashonokk.dvdrental.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationListener {
    private final NotificationService notificationService;

    @EventListener(UserRegistrationCompletedEvent.class)
    public void sendNotification(UserRegistrationCompletedEvent event) {
        notificationService.send(event.getUserTokenDto());
    }
}
