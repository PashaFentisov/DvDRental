package com.pashonokk.dvdrental.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthorizationResponse {
    private AuthorizationToken authorizationToken;
    private String role;
    //todo додати поле permissions
}
