package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RentalDto {
    private Long id;
    private OffsetDateTime rentalDate;
    private OffsetDateTime returnDate;
    private OffsetDateTime lastUpdate;
    private CustomerDto customer;
    private StaffDto staff;
    private InventoryDto inventory;
    private Boolean isDeleted;

}
