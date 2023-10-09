package com.pashonokk.dvdrental.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private ContactInfoDto contactInfo;
}
