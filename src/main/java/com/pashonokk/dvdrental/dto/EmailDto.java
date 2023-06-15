package com.pashonokk.dvdrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailDto {
    @Email(message = "You entered wrong to email")
    @NotEmpty(message = "Field to cannot be empty")
    private String to;
    @Email(message = "You entered wrong from email")
    @NotEmpty(message = "Field from cannot be empty")
    public static final String FROM = System.getenv("EMAIL_FROM");
    @Email(message = "You entered wrong cc email")
    private String cc;
    @Email(message = "You entered wrong bcc email")
    private String bcc;
    private String body;
    private String subject;
}
