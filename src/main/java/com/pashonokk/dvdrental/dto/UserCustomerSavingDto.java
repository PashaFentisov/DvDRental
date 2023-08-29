package com.pashonokk.dvdrental.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCustomerSavingDto {
    @Email(message = "You entered wrong email")
    private String email;
    @NotBlank(message = "Password can`t be empty or null")
    @Size(min = 10, message = "Password must have more than 10 characters")
    private String password;
    @Valid
    @NotNull(message = "Address can`t be null")
    private AddressSavingDto address;
    @Valid
    @NotNull(message = "Contact info can`t be null")
    private ContactInfoDto contactInfo;
}
