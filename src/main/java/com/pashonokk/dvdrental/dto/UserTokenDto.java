package com.pashonokk.dvdrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTokenDto {
    private String tokenValue;
    private String userEmail;
}
