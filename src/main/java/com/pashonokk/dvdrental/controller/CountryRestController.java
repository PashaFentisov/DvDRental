package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryRestController {
    private final CountryService countryService;

    @GetMapping("{id}")
    public ResponseEntity<CountryDto> getCountry(@PathVariable Long id) {
        CountryDto countryDto = countryService.getCountry(id);
        return ResponseEntity.ok(countryDto);
    }

    @GetMapping
    public ResponseEntity<Page<CountryDto>> getCountries(@RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "10") int size,
                                                         @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 elements");
        }
        Page<CountryDto> countries = countryService.getCountries(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(countries);
    }

    @GetMapping("{id}/cities")
    public ResponseEntity<List<CityDto>> getCountryCities(@PathVariable Long id) {
        List<CityDto> countryCities = countryService.getCountryCities(id);
        return ResponseEntity.ok(countryCities);
    }

    @PostMapping
    public ResponseEntity<CountryDto> addCountry(@RequestBody CountryDto countryDto) {
        CountryDto savedCountryDto = countryService.addCountry(countryDto);
        return ResponseEntity.created(URI.create("localhost:10000/countries/" + savedCountryDto.getId())).body(savedCountryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCountryName(@PathVariable Long id, @RequestParam String name) {
        countryService.updateCountryName(name, id);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> partiallyUpdateCountry(@PathVariable Long id, @RequestBody CountryDto countryDto) {
        countryDto.setId(id);
        countryService.updateCountry(countryDto);
        return ResponseEntity.status(204).build();
    }
}
