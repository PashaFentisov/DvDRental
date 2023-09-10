package com.pashonokk.dvdrental.integration.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pashonokk.dvdrental.dto.MultiplePaymentSavingDto;
import com.pashonokk.dvdrental.dto.PaymentDto;
import com.pashonokk.dvdrental.endpoint.BaseResponse;
import com.pashonokk.dvdrental.util.PaymentBuilder;
import com.pashonokk.dvdrental.util.TestHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentSavingTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestHelper testHelper;

    @Test
    @DisplayName("Create Payment when everything is correct then save")
    void createPaymentWhenCorrectThenSave() {
        HttpHeaders staffTokenHeaders = testHelper.preparePaymentSaving();
        MultiplePaymentSavingDto payment = PaymentBuilder.constructPayment(testHelper.getSavedFilmId(),
                testHelper.getSavedCustomerId(),
                TestHelper.RENTAL_DAYS_FOR_NOT_EXPIRED_PAYMENT);
        HttpEntity<MultiplePaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        ParameterizedTypeReference<List<PaymentDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<PaymentDto>> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                responseType
        );
        List<PaymentDto> savedPayments = savedPaymentResponse.getBody();

        assertEquals(HttpStatus.CREATED, savedPaymentResponse.getStatusCode());
        assertEquals(1, Objects.requireNonNull(savedPaymentResponse.getBody()).size());
        assertFalse(Objects.requireNonNull(savedPayments).get(0).getIsClosed());
        assertNotNull(savedPayments.get(0).getPaymentDate());
    }

    @Test
    @DisplayName("Create two Payments when everything is correct then save")
    void createTwoPaymentWhenCorrectThenSave() {
        List<Long> filmIds = new ArrayList<>();
        HttpHeaders staffTokenHeaders = testHelper.preparePaymentSaving();
        filmIds.add(testHelper.getSavedFilmId());
        for (int i = 0; i < 1; i++) {
            testHelper.saveNewLanguageAndFilm(staffTokenHeaders);
            filmIds.add(testHelper.getSavedFilmId());
        }
        MultiplePaymentSavingDto payment = PaymentBuilder.constructManyPayments(filmIds, testHelper.getSavedCustomerId(),
                TestHelper.RENTAL_DAYS_FOR_NOT_EXPIRED_PAYMENT);
        HttpEntity<MultiplePaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);
        ParameterizedTypeReference<List<PaymentDto>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<PaymentDto>> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                responseType
        );
        List<PaymentDto> savedPayments = savedPaymentResponse.getBody();

        assertEquals(HttpStatus.CREATED, savedPaymentResponse.getStatusCode());
        assertEquals(2, Objects.requireNonNull(savedPaymentResponse.getBody()).size());
        assertFalse(Objects.requireNonNull(savedPayments).get(0).getIsClosed());
        assertFalse(Objects.requireNonNull(savedPayments).get(1).getIsClosed());
        assertNotNull(savedPayments.get(0).getPaymentDate());
        assertNotNull(savedPayments.get(1).getPaymentDate());
    }


    @Test
    @DisplayName("Create Payment when unauthorized then 403")
    void createPaymentWhenUnauthorizedThenFail() {
        MultiplePaymentSavingDto payment = PaymentBuilder.constructPayment(testHelper.getSavedFilmId(),
                testHelper.getSavedCustomerId(),
                TestHelper.RENTAL_DAYS_FOR_NOT_EXPIRED_PAYMENT);
        HttpEntity<MultiplePaymentSavingDto> requestEntity = new HttpEntity<>(payment);

        ResponseEntity<PaymentDto> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                PaymentDto.class
        );

        assertEquals(HttpStatus.FORBIDDEN, savedPaymentResponse.getStatusCode());

    }

    @SneakyThrows
    @Test
    @DisplayName("Create Payment when customer doesnt exist then return 400")
    void createPaymentWhenCustomerDoesntExistThenFail() {
        HttpHeaders staffTokenHeaders = testHelper.preparePaymentSaving();
        MultiplePaymentSavingDto payment = PaymentBuilder.constructPayment(testHelper.getSavedFilmId(),
                0L,
                TestHelper.RENTAL_DAYS_FOR_NOT_EXPIRED_PAYMENT);
        HttpEntity<MultiplePaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        ResponseEntity<String> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        BaseResponse failureResponse = objectMapper.readValue(savedPaymentResponse.getBody(), BaseResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, savedPaymentResponse.getStatusCode());
        assertEquals(String.format("Customer with id %s doesn't exist", 0L), failureResponse.getMessage());
    }


    @SneakyThrows
    @Test
    @DisplayName("Create Payment when inventory doesnt exist then return 400")
    void createPaymentWhenInventoryDoesntExistThenFail() {
        HttpHeaders staffTokenHeaders = testHelper.preparePaymentSaving();
        MultiplePaymentSavingDto payment = PaymentBuilder.constructPayment(0L,
                testHelper.getSavedCustomerId(),
                TestHelper.RENTAL_DAYS_FOR_NOT_EXPIRED_PAYMENT);
        HttpEntity<MultiplePaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        ResponseEntity<String> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        BaseResponse failureResponse = objectMapper.readValue(savedPaymentResponse.getBody(), BaseResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, savedPaymentResponse.getStatusCode());
        assertEquals(String.format("Inventory with film id %s and store id %s doesn't exist", 0, testHelper.getSavedStoreId()), failureResponse.getMessage());
    }

}
