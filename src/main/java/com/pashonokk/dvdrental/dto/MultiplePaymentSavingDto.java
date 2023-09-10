package com.pashonokk.dvdrental.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultiplePaymentSavingDto {
    @NotNull(message = "Customer id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long customerId;
    @Size(max = 3, message = "You can`t rent mmore than 3 films at one time")
    private Set<@Valid RentalRequestDto> rentals;
}
