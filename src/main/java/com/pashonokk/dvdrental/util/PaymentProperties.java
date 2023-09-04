package com.pashonokk.dvdrental.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "dvd.oneday")
@Configuration
@Data
public class PaymentProperties {
    private BigDecimal price;
    private BigDecimal fine;
}
