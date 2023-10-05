package com.pashonokk.dvdrental.batch.batchInitV1.country;

import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("countryItemWriter")
@RequiredArgsConstructor
public class CountryItemWriter implements ItemWriter<CountryData> {
    private final CountryService countryService;
    @Override
    public void write(Chunk<? extends CountryData> chunk){
        List<? extends CountryData> countries = chunk.getItems();
        for (CountryData country : countries) {
            CountryDto countryDto = new CountryDto();
            countryDto.setName(country.getName());
            countryService.addCountry(countryDto);
        }
    }
}
