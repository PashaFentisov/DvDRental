package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
    private Long id;
    private AddressDto addressDto;
    private LocalDate lastUpdate;
}
