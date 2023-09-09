package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.HolidayDto;
import com.pashonokk.dvdrental.dto.HolidaySavingDto;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.HolidayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/holidays")
public class HolidayController {
    private final HolidayService holidayService;
    private final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).SAVE_HOLIDAYS)")
    public ResponseEntity<HolidayDto> saveHolidays(@RequestBody @Valid HolidaySavingDto holidaySavingDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        HolidayDto savedHolidays = holidayService.saveHolidays(holidaySavingDto);
        return ResponseEntity.status(201).body(savedHolidays);
    }

//    @GetMapping("/byYear/{year}")
//    public ResponseEntity<ActorDto> getActorById(@PathVariable int year) { //todo realize later
//        ActorDto actorDto = actorService.getActorById(id);
//        return ResponseEntity.ok(actorDto);
//    }

}
