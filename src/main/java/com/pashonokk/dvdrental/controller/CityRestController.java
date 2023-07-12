package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CitySavingDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityRestController {
    private final CityService cityService;

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable Long id) {
        CityDto cityDto = cityService.getCityById(id);
        return ResponseEntity.ok(cityDto);
    }

    @GetMapping
    public ResponseEntity<Page<CityDto>> getCities(@RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int size,
                                                   @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 elements");
        }
        Page<CityDto> cities = cityService.getCities(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(cities);
    }

    @PostMapping("{id}")
    public ResponseEntity<CityDto> addCity(@PathVariable Long id, @RequestBody CitySavingDto citySavingDto) {
        citySavingDto.setCountryId(id);
        CityDto savedCityDto = cityService.saveCity(citySavingDto);
        return ResponseEntity.created(URI.create("localhost:10000/countries/" + savedCityDto.getId())).body(savedCityDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCityById(@PathVariable Long id) {
        cityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> partiallyUpdateCity(@PathVariable Long id, @RequestBody CityDto cityDto) {
        cityDto.setId(id);
        if (!cityService.partiallyUpdateCity(cityDto)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(204).build();
    }
}
