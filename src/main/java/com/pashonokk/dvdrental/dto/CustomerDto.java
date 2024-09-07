package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime createDate;
    private AddressDto address;
    private Boolean isDeleted;
    private UserDto user;
    private BigDecimal balance;
}
