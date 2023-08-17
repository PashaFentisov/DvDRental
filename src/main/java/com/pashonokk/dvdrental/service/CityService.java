package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CitySavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.City;
import com.pashonokk.dvdrental.entity.Country;
import com.pashonokk.dvdrental.mapper.CityMapper;
import com.pashonokk.dvdrental.mapper.CitySavingMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final PageMapper pageMapper;
    private final CitySavingMapper citySavingMapper;
    private final CountryRepository countryRepository;
    private static final String CITY_ERROR_MESSAGE = "City with id %s doesn't exist";
    private static final String COUNTRY_ERROR_MESSAGE = "Country with id %s doesn't exist";


    @Transactional(readOnly = true)
    public CityDto getCityById(Long id) {
        return cityRepository.findByIdWithCountry(id).map(cityMapper::toDto)
                .orElseThrow(()->new EntityNotFoundException(String.format(CITY_ERROR_MESSAGE, id)));
    }

    @Transactional(readOnly = true)
    public PageResponse<CityDto> getCities(Pageable pageable) {
        return pageMapper.toPageResponse(cityRepository.findAll(pageable).map(cityMapper::toDto));
    }

    @Transactional
    public CityDto saveCity(CitySavingDto citySavingDto) {
        Country country = countryRepository.findByIdWithCities(citySavingDto.getCountryId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(COUNTRY_ERROR_MESSAGE,citySavingDto.getCountryId())));
        City city = citySavingMapper.toEntity(citySavingDto);
        city.setLastUpdate(OffsetDateTime.now());
        country.addCity(city);
        City savedCity = cityRepository.save(city);
        return cityMapper.toDto(savedCity);
    }

    @Transactional
    public void deleteById(Long id) {
        City city = cityRepository.findByIdWithAddresses(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CITY_ERROR_MESSAGE, id)));
        city.removeAddresses(city.getAddresses());
        cityRepository.deleteById(id);
    }
}
