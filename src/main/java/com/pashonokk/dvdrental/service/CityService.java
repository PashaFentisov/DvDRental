package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CitySavingDto;
import com.pashonokk.dvdrental.entity.City;
import com.pashonokk.dvdrental.entity.Country;
import com.pashonokk.dvdrental.exception.CountryNotFoundException;
import com.pashonokk.dvdrental.mapper.CityMapper;
import com.pashonokk.dvdrental.mapper.CitySavingMapper;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final CitySavingMapper citySavingMapper;
    private final CountryRepository countryRepository;

    @Transactional(readOnly = true)
    public CityDto getCityById(Long id) {
        return cityRepository.findById(id).map(cityMapper::toDto).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<CityDto> getCities(Pageable pageable) {
        return cityRepository.findAll(pageable).map(cityMapper::toDto);
    }

    @Transactional
    public CityDto saveCity(CitySavingDto citySavingDto) {
        Country country = countryRepository.findByIdWithCities(citySavingDto.getCountryId())
                .orElseThrow(() -> new CountryNotFoundException("Such country does not exist"));
        City city = citySavingMapper.toEntity(citySavingDto);
        country.addCity(city);
        City savedCity = cityRepository.save(city);
        return cityMapper.toDto(savedCity);
    }

    @Transactional
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }

    @Transactional
    public boolean partiallyUpdateCity(CityDto cityDto) {
        City city = cityRepository.findById(cityDto.getId()).orElse(null);
        if (city == null) {
            return false;
        }
        if (cityDto.getName() != null) {
            city.setName(cityDto.getName());
        }
        if (cityDto.getLastUpdate() != null) {
            city.setLastUpdate(cityDto.getLastUpdate());
        }
        return true;
    }
}
