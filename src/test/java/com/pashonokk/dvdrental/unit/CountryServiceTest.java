package com.pashonokk.dvdrental.unit;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.City;
import com.pashonokk.dvdrental.entity.Country;
import com.pashonokk.dvdrental.mapper.CityMapper;
import com.pashonokk.dvdrental.mapper.CountryMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.mapper.impl.PageMapperImpl;
import com.pashonokk.dvdrental.repository.CountryRepository;
import com.pashonokk.dvdrental.service.CountryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {
    @Mock
    CountryRepository countryRepository;
    @Spy
    CountryMapper countryMapper = Mappers.getMapper(CountryMapper.class);
    PageMapper pageMapper = new PageMapperImpl();
    @Spy
    CityMapper cityMapper = Mappers.getMapper(CityMapper.class);
    CountryService countryService;
    private static final Long ID = 1L;


    @BeforeEach
    void countryServiceInit() {
        this.countryService = new CountryService(countryRepository, countryMapper, pageMapper, cityMapper);
    }

    @Test
    void getCountryWhenRealIdThenOk() {
        List<Country> countries = buildCountryList();

        Mockito.when(countryRepository.findById(ID)).thenReturn(Optional.of(countries.get(0)));

        CountryDto country = countryService.getCountry(ID);

        assertNotNull(country);
        assertInstanceOf(CountryDto.class, country);
        assertEquals(1, country.getId());
        assertEquals("Ukraine", country.getName());
    }

    @Test
    void getCountryWhenIdDoesntExistThenThrowException() {
        Mockito.when(countryRepository.findById(ID)).thenReturn(Optional.empty());
        var entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> countryService.getCountry(ID));
        assertEquals("Country with id 1 doesn't exist", entityNotFoundException.getMessage());
    }

    @Test
    void addCountry() {
        CountryDto countryToSave = new CountryDto("Ukraine", OffsetDateTime.now());

        List<Country> savedCountries = new ArrayList<>();

        Mockito.when(countryRepository.save(any())).thenAnswer(invocation -> {
            Country a = invocation.getArgument(0);
            a.setId(1L);
            savedCountries.add(a);
            return a;
        });

        CountryDto savedCountryDto = countryService.addCountry(countryToSave);
        assertNotNull(savedCountryDto);
        assertEquals(1, savedCountries.size());
        assertEquals("Ukraine", savedCountryDto.getName());
        assertEquals(1, savedCountryDto.getId());
        assertFalse(savedCountryDto.getIsDeleted());
        assertNotNull(savedCountryDto.getLastUpdate());
    }

    @Test
    void getCountries() {
        Page<Country> pageOfCountries = getPageOfCountries();
        Mockito.when(countryRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pageOfCountries);

        PageResponse<CountryDto> countries = countryService.getCountries(PageRequest.of(1, 1));

        assertEquals(pageOfCountries.getContent(), countries.getRecords().stream().map(countryMapper::toEntity).toList());
        assertEquals(pageOfCountries.getTotalElements(), countries.getTotalRecords());
        assertEquals(1, countries.getPageNumber());
        assertEquals(1, countries.getPageSize());
    }

    private Page<Country> getPageOfCountries() {
        List<Country> countryList = buildCountryList();
        return new PageImpl<>(countryList, PageRequest.of(1, 1), countryList.size());
    }


    @Test
    void getCountryCitiesWhenIdExistGetCities() {
        List<Country> countries = buildCountryList();

        Mockito.when(countryRepository.findByIdWithCities(ID)).thenReturn(Optional.of(countries.get(0)));
        Mockito.when(cityMapper.toDto(any())).thenAnswer(invocation -> {
            City dto = invocation.getArgument(0);
            return new CityDto(dto.getName(), dto.getLastUpdate());
        });

        List<CityDto> countryCities = countryService.getCountryCities(ID);

        assertNotNull(countryCities);
        assertEquals(countries.get(0).getCities().size(), countryCities.size());
        assertEquals("Kyiv", countryCities.get(0).getName());
    }
    @Test
    void deleteCountryWhenIdExistsIsDeletedTrue() {
        List<Country> countries = buildCountryList();

        Mockito.when(countryRepository.findByIdWithCities(ID)).thenReturn(Optional.of(countries.get(0)));

        countryService.deleteCountry(ID);
        assertTrue(countries.get(0).getIsDeleted());
    }

    @Test
    void deleteCountryWhenIdDoesntExistThrowException() {
        Mockito.when(countryRepository.findByIdWithCities(ID)).thenReturn(Optional.empty());
        var entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> countryService.deleteCountry(ID));
        assertEquals("Country with id 1 doesn't exist", entityNotFoundException.getMessage());
    }

    @Test
    void getCountryCitiesWhenIdDoesntExistThrowException() {
        Mockito.when(countryRepository.findByIdWithCities(ID)).thenReturn(Optional.empty());
        var entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> countryService.getCountryCities(ID));
        Mockito.verifyNoInteractions(cityMapper);
        assertEquals("Country with id 1 doesn't exist", entityNotFoundException.getMessage());
    }

    private List<Country> buildCountryList() {
        List<Country> countries = new ArrayList<>();
        Country ukraine = new Country("Ukraine", OffsetDateTime.now());
        Country usa = new Country("USA", OffsetDateTime.now());
        Country britain = new Country("Britain", OffsetDateTime.now());
        City kyiv = new City("Kyiv", OffsetDateTime.now());
        ukraine.getCities().add(kyiv);
        countries.add(ukraine);
        countries.add(usa);
        countries.add(britain);
        for (int i = 0, j = 1; i < countries.size(); i++, j++) {
            countries.get(i).setId((long) j);
        }
        return countries;
    }
}