package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.dto.PaymentSavingDto;

import java.util.Random;

public class PaymentBuilder {
    private final static Random RANDOM = new Random();

    public static PaymentSavingDto constructPayment(Long filmId, Long customerId) {
        return PaymentSavingDto.builder()
                .customerId(customerId)
                .rentalDays(RANDOM.nextInt(30))
                .filmId(filmId)
                .build();
    }
}
