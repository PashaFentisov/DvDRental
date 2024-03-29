package com.pashonokk.dvdrental.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInfoDto {
    @NotBlank(message = "First name can`t be empty or null")
    private String firstName;
    @NotBlank(message = "Last name can`t be empty or null")
    private String lastName;
    @NotBlank(message = "Username can`t be empty or null")
    private String username;
    private Boolean isActive;

}
