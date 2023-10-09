package com.pashonokk.dvdrental.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSavingDto {
    private Long customerId;
    private Long filmId;
    private int rentalDays;
}
