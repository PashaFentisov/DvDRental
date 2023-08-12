package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressSavingDto {
    private Long id;
    private int houseNumber;
    private String street;
    private String district;
    private int postalCode;
    private OffsetDateTime lastUpdate;
    private String phone;
    private Long cityId;
}
