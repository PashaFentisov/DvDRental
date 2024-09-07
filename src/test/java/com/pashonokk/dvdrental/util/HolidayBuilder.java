package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.dto.HolidaySavingDto;
import com.pashonokk.dvdrental.dto.Month;

import java.time.LocalDate;
import java.util.List;


public class HolidayBuilder {

    public static HolidaySavingDto constructHoliday() {
        int today = LocalDate.now().getDayOfMonth();
        Month month = Month.builder()
                .number(LocalDate.now().getMonthValue())
                .days(List.of(today))
                .build();
        return HolidaySavingDto.builder()
                .year(LocalDate.now().getYear())
                .months(List.of(month))
                .build();
    }
}
