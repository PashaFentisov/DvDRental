package com.pashonokk.dvdrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Email(message = "You entered wrong email")
    private String email;
    @NotEmpty(message = "Enter password")
    @Size(min = 10, message = "Size of password must be more than 10 characters")
    private String password;
}
