package com.pashonokk.dvdrental.dto;


import lombok.*;

import java.time.LocalDate;

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
    private LocalDate lastUpdate;
    private LocalDate createDate;
    private AddressSavingDto addressSavingDto;
    private boolean active;
}
