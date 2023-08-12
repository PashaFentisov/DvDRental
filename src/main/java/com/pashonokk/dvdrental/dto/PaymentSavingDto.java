package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSavingDto {
    private Long id;
    private BigDecimal amount;
    private OffsetDateTime paymentDate;
    private Long customerId;
    private Long staffId;
    private Long rentalId;
}
