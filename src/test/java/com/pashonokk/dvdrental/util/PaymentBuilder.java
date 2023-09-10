package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.dto.MultiplePaymentSavingDto;
import com.pashonokk.dvdrental.dto.RentalRequestDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PaymentBuilder {
    public static MultiplePaymentSavingDto constructPayment(Long filmId, Long customerId, int rentalDays) {
        Set<RentalRequestDto> rentals = new HashSet<>();
        rentals.add(new RentalRequestDto(filmId, rentalDays));
        return MultiplePaymentSavingDto.builder()
                .customerId(customerId)
                .rentals(rentals)
                .build();
    }

    public static MultiplePaymentSavingDto constructManyPayments(List<Long> filmIds, Long customerId, int rentalDays) {
        Set<RentalRequestDto> rentals = new HashSet<>();
        for (Long filmId : filmIds) {
            rentals.add(new RentalRequestDto(filmId, rentalDays));
        }
        return MultiplePaymentSavingDto.builder()
                .customerId(customerId)
                .rentals(rentals)
                .build();
    }
}
