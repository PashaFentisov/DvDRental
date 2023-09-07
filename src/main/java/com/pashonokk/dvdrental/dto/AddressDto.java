package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

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
    private OffsetDateTime lastUpdate;
    private CityDto city;
    private Boolean isDeleted;
    private Set<PhoneDto> phones;
}
