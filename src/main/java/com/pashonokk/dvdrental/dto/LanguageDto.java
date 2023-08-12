package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {
    private Long id;
    private String name;
    private OffsetDateTime lastUpdate;
}
