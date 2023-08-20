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
    private String firstName;
    private String lastName;
    private String email;
    private Boolean active;
    private String username;
    private String password;
    private String pictureUrl;
    private OffsetDateTime lastUpdate;
    private AddressDto address;
    private StoreDto store;
    private Boolean isDeleted;

}
