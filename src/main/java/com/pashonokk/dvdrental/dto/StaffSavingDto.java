package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StaffSavingDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean active;
    private String username;
    private String password;
    private String pictureUrl;
    private OffsetDateTime lastUpdate;
    private AddressSavingDto addressSavingDto;
    private Long storeId;
}
