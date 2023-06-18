package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.entity.City;
import com.pashonokk.dvdrental.mapper.CityMapper;
import com.pashonokk.dvdrental.repository.CityRepository;
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

    @Transactional(readOnly = true)
    public CityDto getCityById(Long id) {
        return cityRepository.findById(id).map(cityMapper::toDto).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<CityDto> getCities(Pageable pageable) {
        return cityRepository.findAll(pageable).map(cityMapper::toDto);
    }

    @Transactional
    public void saveCity(CityDto cityDto) {
//        Country country = cityDto.getCountry();
//        country.setCities(Set.of(cityMapper.toEntity(cityDto)));  //todo how to do fix this relationships
        cityRepository.save(cityMapper.toEntity(cityDto));   //todo add check if city has country
    }

    @Transactional

    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }

    @Transactional
    public void partiallyUpdateCity(CityDto cityDto) {
        City city = cityRepository.findById(cityDto.getId()).orElse(null);
        if (city == null) {
            return;
        }
        if (cityDto.getCity() != null) {
            city.setCity(cityDto.getCity());
        }
        if (cityDto.getLastUpdate() != null) {
            city.setLastUpdate(cityDto.getLastUpdate());
        }
        if (cityDto.getCountry() != null) {
            city.setCountry(cityDto.getCountry());
        }
    }
}