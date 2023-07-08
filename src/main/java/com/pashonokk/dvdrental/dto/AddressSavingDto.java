package com.pashonokk.dvdrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressSavingDto {
    private Long id;
    private int houseNumber;
    private String street;
    private String district;
    private int postalCode;
    private LocalDate lastUpdate;
    private String phone;
    private Long customerId;
}
