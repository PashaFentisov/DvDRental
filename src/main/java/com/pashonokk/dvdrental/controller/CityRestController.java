package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CitySavingDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityRestController {
    private static final String REDIRECT_TO_ALL = "/countries";
    private final CityService cityService;

    @GetMapping("/{id}")
    public CityDto getCityById(@PathVariable Long id) {
        return cityService.getCityById(id);
    }

    @GetMapping
    public Page<CityDto> getCities(@RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "10") int size,
                                   @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 elements");
        }
        return cityService.getCities(PageRequest.of(page, size, Sort.by(sort)));
    }

    @PostMapping("{id}")
    public RedirectView addCity(@PathVariable Long id, @RequestBody CitySavingDto citySavingDto) {
        citySavingDto.setCountryId(id);
        cityService.saveCity(citySavingDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCityById(@PathVariable Long id) {
        cityService.deleteById(id);
        return new RedirectView(REDIRECT_TO_ALL);
    }


    @PatchMapping("/{id}")
    public RedirectView partiallyUpdateCity(@PathVariable Long id, @RequestBody CityDto cityDto) {
        cityDto.setId(id);
        cityService.partiallyUpdateCity(cityDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }
}
