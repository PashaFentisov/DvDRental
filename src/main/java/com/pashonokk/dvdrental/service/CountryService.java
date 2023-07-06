package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.entity.Country;
import com.pashonokk.dvdrental.mapper.CountryMapper;
import com.pashonokk.dvdrental.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Transactional(readOnly = true)
    public CountryDto getCountry(Long id) {
        return countryRepository.findByIdWithCities(id).map(countryMapper::toDto).orElseThrow();
    }

    @Transactional
    public void addCountry(CountryDto countryDto) {
        countryRepository.save(countryMapper.toEntity(countryDto));
    }

    @Transactional(readOnly = true)
    public Page<CountryDto> getCountries(Pageable pageable) {
        return countryRepository.findAll(pageable).map(countryMapper::toDto);
    }

    @Transactional
    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }

    @Transactional
    public void updateCountry(CountryDto countryDto) {
        Country country = countryRepository.findById(countryDto.getId()).orElse(null);
        if (country == null) {
            return;
        }
        if (countryDto.getLastUpdate() != null) {
            country.setLastUpdate(countryDto.getLastUpdate());
        }
        if (countryDto.getName() != null) {
            country.setName(countryDto.getName());
        }
    }

    @Transactional
    public void updateCountryName(String name, Long id) {
        Country country = countryRepository.findById(id).orElse(null);
        if (country == null) {
            return;
        }
        country.setName(name);
    }
}
