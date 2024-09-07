package com.pashonokk.dvdrental.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneSavingDto {
    @NotBlank(message = "Phone number can`t be empty or null")
    private String number;
}
