package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryRestController {
    private static final String REDIRECT_TO_ALL = "/countries";
    private final CountryService countryService;

    @GetMapping("{id}")
    public CountryDto getCountry(@PathVariable Long id) {
        return countryService.getCountry(id);
    }

    @GetMapping
    public Page<CountryDto> getCountries(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size,
                                         @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 elements");
        }
        return countryService.getCountries(PageRequest.of(page, size, Sort.by(sort)));
    }

    @PostMapping
    public RedirectView addCountry(@RequestBody CountryDto countryDto) {
        countryService.addCountry(countryDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PutMapping("/{id}")
    public RedirectView updateCountryName(@PathVariable Long id, @RequestParam String name) {
        countryService.updateCountryName(name, id);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PatchMapping("/{id}")
    public RedirectView partiallyUpdateCountry(@PathVariable Long id, @RequestBody CountryDto countryDto) {
        countryDto.setId(id);
        countryService.updateCountry(countryDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }

}
