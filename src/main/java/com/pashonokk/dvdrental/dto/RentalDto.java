package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RentalDto {
    private Long id;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate lastUpdate;
    private CustomerDto customer;
    private StaffDto staff;
    private InventoryDto inventory;
}
