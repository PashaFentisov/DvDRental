package com.pashonokk.dvdrental.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSavingDto {
    private Long id;
    @NotNull(message = "Customer id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long customerId;
    @NotNull(message = "Film id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long filmId;
    @NotNull(message = "Can`t be empty or null")
    @Max(value = 30, message = "You can rent film maximum for 30 days")
    private int rentalDays;
}
