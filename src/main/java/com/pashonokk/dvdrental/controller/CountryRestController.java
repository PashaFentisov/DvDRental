package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryRestController {
    private final CountryService countryService;
    private final Logger logger = LoggerFactory.getLogger(CountryRestController.class);

    @GetMapping("{id}")
    public ResponseEntity<CountryDto> getCountry(@PathVariable Long id) {
        CountryDto countryDto = countryService.getCountry(id);
        return ResponseEntity.ok(countryDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CountryDto>> getCountries(@RequestParam(required = false, defaultValue = "0") int page,
                                                                 @RequestParam(required = false, defaultValue = "10") int size,
                                                                 @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 countries at one time");
        }
        PageResponse<CountryDto> countries = countryService.getCountries(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(countries);
    }

    @GetMapping("{id}/cities")
    public ResponseEntity<List<CityDto>> getCountryCities(@PathVariable Long id) {
        List<CityDto> countryCities = countryService.getCountryCities(id);
        return ResponseEntity.ok(countryCities);
    }

    @PostMapping
    public ResponseEntity<CountryDto> addCountry(@RequestBody @Valid CountryDto countryDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        CountryDto savedCountry = countryService.addCountry(countryDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCountry.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCountry);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).DELETE_ACCESS)")
    public ResponseEntity<Object> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }

}
