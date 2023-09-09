package com.pashonokk.dvdrental.dto;

import com.pashonokk.dvdrental.enumeration.Month;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidaySavingDto {
    @Min(value = 2020, message = "Year can`t be less than 2020")
    private int year;
    private List<@Valid Month> months;
}
