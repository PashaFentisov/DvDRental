package com.pashonokk.dvdrental.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "sendgrid")
@Configuration
@Data
public class SendGridProperties {
    /**
     * api key for SendGrid
     */
    private String key;
}
