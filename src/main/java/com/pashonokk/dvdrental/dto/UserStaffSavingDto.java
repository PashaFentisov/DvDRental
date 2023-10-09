package com.pashonokk.dvdrental.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStaffSavingDto {
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
    private String pictureUrl;
    @NotNull(message = "Store id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long storeId;
}
