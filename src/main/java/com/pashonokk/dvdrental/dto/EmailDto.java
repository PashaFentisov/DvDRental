package com.pashonokk.dvdrental.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDto {
    private String to;
    private String from;
    private String cc;
    private String bcc;
    private String body;
    private String subject;
}
