package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.entity.Country;
import com.pashonokk.dvdrental.exception.CountryNotFoundException;
import com.pashonokk.dvdrental.mapper.CityMapper;
import com.pashonokk.dvdrental.mapper.CountryMapper;
import com.pashonokk.dvdrental.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final CityMapper cityMapper;

    @Transactional(readOnly = true)
    public CountryDto getCountry(Long id) {
        return countryRepository.findById(id)
                .map(countryMapper::toDto)
                .orElseThrow(() -> new CountryNotFoundException("Country with id " + id + " doesn't exist"));
    }

    @Transactional
    public CountryDto addCountry(CountryDto countryDto) {
        Country savedCountry = countryRepository.save(countryMapper.toEntity(countryDto));
        return countryMapper.toDto(savedCountry);
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
        Country country = countryRepository.findById(countryDto.getId())
                .orElseThrow(()->new CountryNotFoundException("Country with id " + countryDto.getId() + " doesn't exist"));
        if (countryDto.getLastUpdate() != null) {
            country.setLastUpdate(countryDto.getLastUpdate());
        }
        if (countryDto.getName() != null) {
            country.setName(countryDto.getName());
        }
    }

    @Transactional
    public void updateCountryName(String name, Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(()->new CountryNotFoundException("Country with id " + id + " doesn't exist"));
        country.setName(name);
    }

    @Transactional(readOnly = true)
    public List<CityDto> getCountryCities(Long id) {
        Country country = countryRepository.findByIdWithCities(id)
                .orElseThrow(() -> new CountryNotFoundException("Country with id " + " does`nt exist"));
        return country.getCities().stream().map(cityMapper::toDto).toList();
    }
}
