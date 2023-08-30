package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.dto.PaymentSavingDto;

import java.util.Random;

public class PaymentBuilder {
    private final static Random RANDOM = new Random();

    public static PaymentSavingDto constructPayment(Long filmId, Long storeId, Long customerId) {
        return PaymentSavingDto.builder()
                .customerId(customerId)
                .rentalDays(RANDOM.nextInt(30))
                .storeId(storeId)
                .filmId(filmId)
                .build();
    }
}
