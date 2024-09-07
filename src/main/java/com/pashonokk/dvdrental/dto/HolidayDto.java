package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDto {
    private List<OffsetDateTime> holidays;
}
