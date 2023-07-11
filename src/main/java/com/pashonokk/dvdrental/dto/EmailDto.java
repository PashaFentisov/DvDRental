package com.pashonokk.dvdrental.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    private String to;
    public static final String FROM = System.getenv("EMAIL_FROM");
    private String cc;
    private String bcc;
    private String body;
    private String subject;
}
