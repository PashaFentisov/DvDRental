package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.dto.PageDto;
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
    public ResponseEntity<Page<CountryDto>> getCountries(@RequestBody(required = false) PageDto pageDto) {
        if (pageDto.getSize() > 100) {
            throw new BigSizeException("You can get maximum 100 elements");
        }
        Page<CountryDto> countries = countryService.getCountries(PageRequest.of(pageDto.getPage(), pageDto.getSize(), Sort.by(pageDto.getSort())));
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
    public ResponseEntity<CountryDto> partiallyUpdateCountry(@PathVariable Long id, @RequestBody CountryDto countryDto) {
        countryDto.setId(id);
        CountryDto updatedCountry = countryService.updateCountry(countryDto);
        return ResponseEntity.ok(updatedCountry);
    }
}
