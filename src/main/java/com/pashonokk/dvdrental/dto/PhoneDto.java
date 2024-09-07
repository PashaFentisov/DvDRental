package com.pashonokk.dvdrental.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDto {
    private Long id;
    private String number;
    private boolean isMain;
    private boolean isDeleted;
}
