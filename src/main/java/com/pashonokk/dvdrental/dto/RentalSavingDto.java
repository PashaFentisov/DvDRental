package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RentalSavingDto {
    private Long id;
    private OffsetDateTime rentalDate;
    private OffsetDateTime returnDate;
    private OffsetDateTime lastUpdate;
    private Long customerId;
    private Long staffId;
    private Long inventoryId;
}
