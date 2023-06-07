package com.pashonokk.dvdrental.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@ConfigurationProperties(prefix = "token.valid")
@Configuration
@Data
@Setter(value = AccessLevel.PRIVATE)
public class TokenProperties {
    /**
     * time while token will be available
     */
    private Duration period;
}
