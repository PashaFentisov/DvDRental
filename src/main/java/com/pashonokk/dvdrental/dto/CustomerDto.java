package com.pashonokk.dvdrental.dto;

import com.pashonokk.dvdrental.entity.Address;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
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
