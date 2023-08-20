package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private ContactInfoDto contactInfo;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime createDate;
    private AddressDto address;
    private Boolean isDeleted;
}
