package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.entity.Country;
import com.pashonokk.dvdrental.mapper.CityMapper;
import com.pashonokk.dvdrental.mapper.CountryMapper;
import com.pashonokk.dvdrental.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new EntityNotFoundException("Country with id " + id + " doesn't exist"));
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
    public CountryDto updateCountry(CountryDto countryDto) {
        Country country = countryRepository.findById(countryDto.getId())
                .orElseThrow(()->new EntityNotFoundException("Country with id " + countryDto.getId() + " doesn't exist"));
        Optional.ofNullable(countryDto.getName()).ifPresent(country::setName);
        Optional.ofNullable(countryDto.getLastUpdate()).ifPresent(country::setLastUpdate);
        return countryMapper.toDto(country);
    }

    @Transactional
    public void updateCountryName(String name, Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Country with id " + id + " doesn't exist"));
        country.setName(name);
    }

    @Transactional(readOnly = true)
    public List<CityDto> getCountryCities(Long id) {
        Country country = countryRepository.findByIdWithCities(id)
                .orElseThrow(() -> new EntityNotFoundException("Country with id " + " does`nt exist"));
        return country.getCities().stream().map(cityMapper::toDto).toList();
    }
}
