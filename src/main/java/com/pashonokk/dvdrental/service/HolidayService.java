package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.HolidayDto;
import com.pashonokk.dvdrental.dto.HolidaySavingDto;
import com.pashonokk.dvdrental.entity.Holiday;
import com.pashonokk.dvdrental.enumeration.Month;
import com.pashonokk.dvdrental.exception.GenericDisplayableException;
import com.pashonokk.dvdrental.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayRepository holidayRepository;

    public HolidayDto saveHolidays(HolidaySavingDto holidaySavingDto) {
        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now());
        List<Holiday> holidays = new ArrayList<>();
        int year = holidaySavingDto.getYear();
        Holiday holiday;
        for (int i = 0; i < holidaySavingDto.getMonths().size(); i++) {
            Month month = holidaySavingDto.getMonths().get(i);
            for (int j = 0; j < month.getDays().size(); j++) {
                LocalDate holidayDate;
                try{
                   holidayDate = LocalDate.of(year, month.getNumber(), month.getDays().get(j));
                }catch (Exception e){
                    throw new GenericDisplayableException(HttpStatus.BAD_REQUEST, "You entered bad day value");
                }
                holiday = new Holiday(OffsetDateTime.of(holidayDate, LocalTime.MIDNIGHT, offset));
                holidays.add(holiday);
            }
        }
        holidayRepository.saveAll(holidays);
        return new HolidayDto(holidays.stream().map(Holiday::getDate).toList());
    }
}
