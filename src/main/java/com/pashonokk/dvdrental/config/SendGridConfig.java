package com.pashonokk.dvdrental.config;

import com.pashonokk.dvdrental.util.SendGridProperties;
import com.sendgrid.SendGrid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SendGridConfig {
    private final SendGridProperties sendGridProperties;

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(sendGridProperties.getKey());
    }
}
