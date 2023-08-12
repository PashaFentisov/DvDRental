package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
    private Long id;
    private AddressDto address;
    private OffsetDateTime lastUpdate;
}
