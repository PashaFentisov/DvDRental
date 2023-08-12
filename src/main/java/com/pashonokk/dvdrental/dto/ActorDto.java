package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String biography;
    private OffsetDateTime birthDate;
    private OffsetDateTime lastUpdate;
}
