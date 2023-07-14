package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private int houseNumber;
    private String street;
    private String district;
    private int postalCode;
    private LocalDate lastUpdate;
    private String phone;
}
