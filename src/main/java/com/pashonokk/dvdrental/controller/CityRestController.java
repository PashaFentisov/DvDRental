package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CitySavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityRestController {
    private final CityService cityService;
    private final Logger logger = LoggerFactory.getLogger(CityRestController.class);

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable Long id) {
        CityDto cityDto = cityService.getCityById(id);
        return ResponseEntity.ok(cityDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CityDto>> getCities(@RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int size,
                                                   @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 cities at one time");
        }
        PageResponse<CityDto> cities = cityService.getCities(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(cities);
    }

    @PostMapping("{id}")
    public ResponseEntity<CityDto> addCity(@PathVariable Long id, @RequestBody @Valid CitySavingDto citySavingDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        citySavingDto.setCountryId(id);
        CityDto savedCity = cityService.saveCity(citySavingDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCity.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCityById(@PathVariable Long id) {
        cityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
