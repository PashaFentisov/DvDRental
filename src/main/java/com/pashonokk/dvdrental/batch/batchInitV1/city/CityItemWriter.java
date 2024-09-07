package com.pashonokk.dvdrental.batch.batchInitV1.city;

import com.pashonokk.dvdrental.dto.CitySavingDto;
import com.pashonokk.dvdrental.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
@Component("cityItemWriter")
@RequiredArgsConstructor
public class CityItemWriter implements ItemWriter<CityData> {
    private final CityService cityService;
    @Override
    public void write(Chunk<? extends CityData> chunk){
        List<? extends CityData> cities = chunk.getItems();
        for (CityData city : cities) {
            CitySavingDto cityDto = new CitySavingDto(city.getName(), city.getCountryId());
            cityDto.setName(city.getName());
            cityService.saveCity(cityDto);
        }
    }
}
