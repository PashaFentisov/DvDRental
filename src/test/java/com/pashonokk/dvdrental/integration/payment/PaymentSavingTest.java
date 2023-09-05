package com.pashonokk.dvdrental.integration.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pashonokk.dvdrental.dto.PaymentDto;
import com.pashonokk.dvdrental.dto.PaymentSavingDto;
import com.pashonokk.dvdrental.endpoint.BaseResponse;
import com.pashonokk.dvdrental.util.PaymentBuilder;
import com.pashonokk.dvdrental.util.PaymentProperties;
import com.pashonokk.dvdrental.util.PaymentTestHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentSavingTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PaymentTestHelper paymentTestHelper;
    @LocalServerPort
    private int port;
    @Autowired
    private PaymentProperties paymentProperties;

    @Test
    @DisplayName("Create Payment when everything is correct then save")
    void createPaymentWhenCorrectThenSave() {
        HttpHeaders staffTokenHeaders = paymentTestHelper.preparePaymentSaving();
        PaymentSavingDto payment = PaymentBuilder.constructPayment(paymentTestHelper.getSavedFilmId(), paymentTestHelper.getSavedCustomerId());
        HttpEntity<PaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        ResponseEntity<PaymentDto> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                PaymentDto.class
        );
        String location = String.format("http://localhost:%s/payments/%s", port,
                Objects.requireNonNull(savedPaymentResponse.getBody()).getId());


        assertEquals(HttpStatus.CREATED, savedPaymentResponse.getStatusCode());
        assertNotNull(savedPaymentResponse.getBody().getId());
        assertFalse(savedPaymentResponse.getBody().getIsClosed());
        assertNotNull(savedPaymentResponse.getBody().getPaymentDate());
        assertEquals(paymentProperties.getPrice().multiply(BigDecimal.valueOf(payment.getRentalDays())), savedPaymentResponse.getBody().getAmount());
        assertEquals(URI.create(location), savedPaymentResponse.getHeaders().getLocation());
    }


    @Test
    @DisplayName("Create Payment when unauthorized then 403")
    void createPaymentWhenUnauthorizedThenFail() {
        PaymentSavingDto payment = PaymentBuilder.constructPayment(paymentTestHelper.getSavedFilmId(), paymentTestHelper.getSavedCustomerId());
        HttpEntity<PaymentSavingDto> requestEntity = new HttpEntity<>(payment);

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
        HttpHeaders staffTokenHeaders = paymentTestHelper.preparePaymentSaving();
        PaymentSavingDto payment = PaymentBuilder.constructPayment(
                paymentTestHelper.getSavedFilmId(), paymentTestHelper.getSavedCustomerId() + 1);
        HttpEntity<PaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        ResponseEntity<String> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        BaseResponse failureResponse = objectMapper.readValue(savedPaymentResponse.getBody(), BaseResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, savedPaymentResponse.getStatusCode());
        assertEquals(String.format("Customer with id %s doesn't exist", paymentTestHelper.getSavedCustomerId() + 1), failureResponse.getMessage());
    }


    @SneakyThrows
    @Test
    @DisplayName("Create Payment when inventory doesnt exist then return 400")
    void createPaymentWhenInventoryDoesntExistThenFail() {
        HttpHeaders staffTokenHeaders = paymentTestHelper.preparePaymentSaving();
        PaymentSavingDto payment = PaymentBuilder.constructPayment(0L,
                paymentTestHelper.getSavedCustomerId());
        HttpEntity<PaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        ResponseEntity<String> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        BaseResponse failureResponse = objectMapper.readValue(savedPaymentResponse.getBody(), BaseResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, savedPaymentResponse.getStatusCode());
        assertEquals(String.format("Inventory with film id %s and store id %s doesn't exist", 0, paymentTestHelper.getSavedStoreId()), failureResponse.getMessage());
    }

}
