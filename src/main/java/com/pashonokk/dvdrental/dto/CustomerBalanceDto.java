package com.pashonokk.dvdrental.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBalanceDto {
    @NotNull(message = "Customer id can`t be empty or null")
    @Min(value = 0, message = "Customer id can`t be negative")
    private Long id;
    @DecimalMin(value = "0.00", message = "Balance should be more than 0.00")
    @NotNull
    private BigDecimal balance;
}
