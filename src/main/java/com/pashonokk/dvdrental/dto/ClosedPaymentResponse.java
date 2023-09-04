package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClosedPaymentResponse {
    private Long customerId;
    private Long storeId;
    private Long filmId;
    private BigDecimal totalAmount;
    private Long extraDays;
    private OffsetDateTime rentalDate;
    private OffsetDateTime returnDate;
    private BigDecimal fineAmount;
}