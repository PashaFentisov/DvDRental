package com.pashonokk.dvdrental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentClosingDto {
    @NotNull(message = "Customer id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long customerId;
    @NotNull(message = "Film id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long filmId;
}