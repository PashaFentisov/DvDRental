package com.pashonokk.dvdrental.integration.phone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.pashonokk.dvdrental.dto.PhoneDto;
import com.pashonokk.dvdrental.dto.PhoneSavingDto;
import com.pashonokk.dvdrental.dto.StoreDto;
import com.pashonokk.dvdrental.dto.StoreSavingDto;
import com.pashonokk.dvdrental.endpoint.BaseResponse;
import com.pashonokk.dvdrental.util.StoreBuilder;
import com.pashonokk.dvdrental.util.TestHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PhoneRestControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private TestHelper testHelper;
    private final static Faker FAKER = new Faker(Locale.ENGLISH);
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Set new phone as main when it exists then ok")
    void setNewPhoneAsMainWhenItExistsThenOk() {
        HttpHeaders headersWithToken = testHelper.constructAdminHttpHeaders();
        StoreSavingDto store = StoreBuilder.constructStore();
        StoreDto storeDto = testHelper.saveStore(store, headersWithToken);
        Long firstPhoneId = storeDto.getAddress().getPhones().stream().findFirst().map(PhoneDto::getId).get();

        ResponseEntity<PhoneDto> addPhoneToAddressResponse = addPhoneToAddress(storeDto.getAddress().getId(),
                createHttpEntityWithPhoneSavingDto(headersWithToken));

        HttpEntity<Object> entityWithHeaders = new HttpEntity<>(headersWithToken);

        ResponseEntity<PhoneDto> newMainPhoneResponse = testRestTemplate.exchange(
                "/phones/{addressId}/newMain/{newId}",
                HttpMethod.PUT,
                entityWithHeaders,
                PhoneDto.class,
                storeDto.getAddress().getId(), Objects.requireNonNull(addPhoneToAddressResponse.getBody()).getId()
        );

        ResponseEntity<PhoneDto> getPhoneResponse = getOldMainPhone(firstPhoneId, entityWithHeaders);

        assertEquals(HttpStatus.OK, newMainPhoneResponse.getStatusCode());
        assertTrue(Objects.requireNonNull(newMainPhoneResponse.getBody()).isMain());
        assertFalse(Objects.requireNonNull(getPhoneResponse.getBody()).isMain());
    }


    @SneakyThrows
    @Test
    @DisplayName("Set new phone as main when it doesn`t exists then 400")
    void setNewPhoneAsMainWhenItDoesntExistThenException() {
        HttpHeaders headersWithToken = testHelper.constructAdminHttpHeaders();
        StoreSavingDto store = StoreBuilder.constructStore();
        StoreDto storeDto = testHelper.saveStore(store, headersWithToken);

        HttpEntity<PhoneSavingDto> requestEntity = createHttpEntityWithPhoneSavingDto(headersWithToken);

        addPhoneToAddress(storeDto.getAddress().getId(), requestEntity);
        HttpEntity<Object> entityWithHeaders = new HttpEntity<>(headersWithToken);

        ResponseEntity<String> newMainPhoneResponse = testRestTemplate.exchange(
                "/phones/{addressId}/newMain/{newId}",
                HttpMethod.PUT,
                entityWithHeaders,
                String.class,
                storeDto.getAddress().getId(), 0L
        );

        BaseResponse failureResponse = objectMapper.readValue(newMainPhoneResponse.getBody(), BaseResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, newMainPhoneResponse.getStatusCode());
        assertEquals(String.format("Phone with id %s doesn't exist", 0L), failureResponse.getMessage());
    }

    private ResponseEntity<PhoneDto> getOldMainPhone(Long firstPhoneId, HttpEntity<Object> entityWithHeaders) {
        return testRestTemplate.exchange(
                "/phones/{id}",
                HttpMethod.GET,
                entityWithHeaders,
                PhoneDto.class,
                firstPhoneId
        );
    }

    private static HttpEntity<PhoneSavingDto> createHttpEntityWithPhoneSavingDto(HttpHeaders headersWithToken) {
        PhoneSavingDto phoneSavingDto = PhoneSavingDto.builder()
                .number(FAKER.phoneNumber().cellPhone())
                .build();
        return new HttpEntity<>(phoneSavingDto, headersWithToken);
    }

    private ResponseEntity<PhoneDto> addPhoneToAddress(Long id, HttpEntity<PhoneSavingDto> requestEntity) {
        return testRestTemplate.exchange(
                "/addresses/{id}/phones",
                HttpMethod.POST,
                requestEntity,
                PhoneDto.class,
                id
        );
    }

}
