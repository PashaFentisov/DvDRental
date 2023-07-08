package com.pashonokk.dvdrental.dto;

import com.pashonokk.dvdrental.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate lastUpdate;
    private LocalDate createDate;
    private Address address;
    private boolean active;
}
