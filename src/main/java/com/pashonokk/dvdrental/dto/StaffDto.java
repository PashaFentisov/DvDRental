package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private Long id;
    private UserDto user;
    private String pictureUrl;
    private OffsetDateTime lastUpdate;
    private AddressDto address;
    private StoreDto store;
    private Boolean isDeleted;

}
