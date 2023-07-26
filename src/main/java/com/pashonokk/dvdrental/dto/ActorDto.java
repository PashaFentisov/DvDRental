package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.LocalDate;

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
    private LocalDate birthDate;
    private LocalDate lastUpdate;
}
