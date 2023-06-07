package com.pashonokk.dvdrental.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@ConfigurationProperties(prefix = "token.valid")
@Configuration
@Data
public class TokenProperties {
    /**
     * time while token will be valid
     */
    private Duration duration;
}
