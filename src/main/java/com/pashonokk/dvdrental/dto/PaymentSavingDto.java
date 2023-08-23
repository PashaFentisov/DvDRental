package com.pashonokk.dvdrental.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pashonokk.dvdrental.annotation.ValidOffsetDateTime;
import com.pashonokk.dvdrental.util.CustomOffsetDateTimeDeserializer;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSavingDto {
    private Long id;
    private BigDecimal amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    @ValidOffsetDateTime
    private OffsetDateTime paymentDate;
    @NotNull(message = "Customer id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long customerId;
    @NotNull(message = "Staff id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long staffId;
    @NotNull(message = "Rental id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long rentalId;
    @AssertFalse(message = "You can`t set isDeleted as true")
    private Boolean isDeleted = false;

}
