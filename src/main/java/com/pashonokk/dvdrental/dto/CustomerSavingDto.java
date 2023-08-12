package com.pashonokk.dvdrental.dto;


import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSavingDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime createDate;
    private AddressSavingDto addressSavingDto;
    private boolean active;
}
