package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Country;
import com.pashonokk.dvdrental.mapper.CityMapper;
import com.pashonokk.dvdrental.mapper.CountryMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final PageMapper pageMapper;
    private final CityMapper cityMapper;

    private static final String ERROR_MESSAGE = "Country with id %s doesn't exist";

    @Transactional(readOnly = true)
    public CountryDto getCountry(Long id) {
        return countryRepository.findById(id)
                .map(countryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
    }

    @Transactional
    public CountryDto addCountry(CountryDto countryDto) {
        Country country = countryMapper.toEntity(countryDto);
        country.setIsDeleted(false);
        country.setLastUpdate(OffsetDateTime.now());
        Country savedCountry = countryRepository.save(countryMapper.toEntity(countryDto));
        return countryMapper.toDto(savedCountry);
    }

    @Transactional(readOnly = true)
    public PageResponse<CountryDto> getCountries(Pageable pageable) {
        return pageMapper.toPageResponse(countryRepository.findAll(pageable).map(countryMapper::toDto));
    }

    @Transactional
    public void deleteCountry(Long id) {
        Country country = countryRepository.findByIdWithCities(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        country.setIsDeleted(true);
    }

    @Transactional(readOnly = true)
    public List<CityDto> getCountryCities(Long id) {
        Country country = countryRepository.findByIdWithCities(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        return country.getCities().stream().map(cityMapper::toDto).toList();
    }
}
