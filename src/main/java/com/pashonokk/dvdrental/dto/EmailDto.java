package com.pashonokk.dvdrental.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailDto {
    private String to;
    public static final String from = System.getenv("EMAIL_FROM");
    private String cc;
    private String bcc;
    private String body;
    private String subject;
}
