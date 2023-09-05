package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.dto.PaymentSavingDto;

public class PaymentBuilder {
    public static PaymentSavingDto constructPayment(Long filmId, Long customerId) {
        return PaymentSavingDto.builder()
                .customerId(customerId)
                .rentalDays(10)
                .filmId(filmId)
                .build();
    }
}
