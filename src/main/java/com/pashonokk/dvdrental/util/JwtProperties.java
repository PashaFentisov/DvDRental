package com.pashonokk.dvdrental.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
@Configuration
@Data
public class JwtProperties {
    /**
     * time while jwt will be valid
     */
    private Duration expiration;
    private String signing;
}
