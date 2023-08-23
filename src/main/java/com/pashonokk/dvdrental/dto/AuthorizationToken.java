package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationToken {
    private String token;
    private OffsetDateTime expiresAt;
}
