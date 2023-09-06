package com.pashonokk.dvdrental.integration.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pashonokk.dvdrental.dto.ClosedPaymentResponse;
import com.pashonokk.dvdrental.dto.PaymentClosingDto;
import com.pashonokk.dvdrental.endpoint.BaseResponse;
import com.pashonokk.dvdrental.util.PaymentProperties;
import com.pashonokk.dvdrental.util.PaymentTestHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentClosingTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PaymentTestHelper paymentTestHelper;
    @Autowired
    private PaymentProperties paymentProperties;

    @Test
    @DisplayName("Close Payment when everything is correct then ok")
    void closePaymentWhenCorrectThenOk() {
        HttpHeaders staffTokenHeaders = paymentTestHelper.savePayment();
        PaymentClosingDto paymentClosingDto = new PaymentClosingDto(paymentTestHelper.getSavedCustomerId(), paymentTestHelper.getSavedFilmId());
        HttpEntity<PaymentClosingDto> requestEntity = new HttpEntity<>(paymentClosingDto, staffTokenHeaders);

        ResponseEntity<ClosedPaymentResponse> closedPaymentResponse = testRestTemplate.exchange(
                "/payments/close",
                HttpMethod.POST,
                requestEntity,
                ClosedPaymentResponse.class
        );

        assertEquals(HttpStatus.OK, closedPaymentResponse.getStatusCode());
        assertEquals(paymentTestHelper.getSavedCustomerId(), Objects.requireNonNull(closedPaymentResponse.getBody()).getCustomer().getId());
        assertEquals(paymentTestHelper.getSavedStoreId(), closedPaymentResponse.getBody().getStore().getId());
        assertEquals(paymentTestHelper.getSavedFilmId(), closedPaymentResponse.getBody().getFilm().getId());
        assertEquals(0, closedPaymentResponse.getBody().getExtraDays());
        assertEquals(paymentTestHelper.getSavedPayment().getAmount(), closedPaymentResponse.getBody().getTotalAmount());
        assertEquals(BigDecimal.ZERO, closedPaymentResponse.getBody().getFineAmount());
        assertNotNull(closedPaymentResponse.getBody().getReturnDate());
        assertNotNull(closedPaymentResponse.getBody().getRentalDate());
    }


    @Test
    @DisplayName("Close Payment when unauthorized then 403")
    void closePaymentWhenUnauthorizedThenFail() {
        PaymentClosingDto paymentClosingDto = new PaymentClosingDto(paymentTestHelper.getSavedCustomerId(), paymentTestHelper.getSavedFilmId());
        HttpEntity<PaymentClosingDto> requestEntity = new HttpEntity<>(paymentClosingDto);

        ResponseEntity<ClosedPaymentResponse> closedPaymentResponse = testRestTemplate.exchange(
                "/payments/close",
                HttpMethod.POST,
                requestEntity,
                ClosedPaymentResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, closedPaymentResponse.getStatusCode());
    }

    @SneakyThrows
    @Test
    @DisplayName("Close Payment with delay then price includes fine")
    void closePaymentWhenWithDelayThenPriceWithFine() {
        HttpHeaders staffTokenHeaders = paymentTestHelper.saveExpiredPayment();
        PaymentClosingDto paymentClosingDto = new PaymentClosingDto(paymentTestHelper.getSavedCustomerId(), paymentTestHelper.getSavedFilmId());
        HttpEntity<PaymentClosingDto> requestEntity = new HttpEntity<>(paymentClosingDto, staffTokenHeaders);
        long extraDays = Duration.between(paymentTestHelper.getSavedPayment().getPaymentDate().toLocalDateTime(), LocalDateTime.now()).toDays();

        ResponseEntity<ClosedPaymentResponse> closedPaymentResponse = testRestTemplate.exchange(
                "/payments/close",
                HttpMethod.POST,
                requestEntity,
                ClosedPaymentResponse.class
        );
        assertEquals(HttpStatus.OK, closedPaymentResponse.getStatusCode());
        assertEquals(paymentTestHelper.getSavedCustomerId(), Objects.requireNonNull(closedPaymentResponse.getBody()).getCustomer().getId());
        assertEquals(paymentTestHelper.getSavedStoreId(), closedPaymentResponse.getBody().getStore().getId());
        assertEquals(paymentTestHelper.getSavedFilmId(), closedPaymentResponse.getBody().getFilm().getId());
        assertEquals(extraDays, closedPaymentResponse.getBody().getExtraDays());
        assertEquals(paymentProperties.getFine().multiply(BigDecimal.valueOf(extraDays)), closedPaymentResponse.getBody().getFineAmount());
        assertNotNull(closedPaymentResponse.getBody().getReturnDate());
        assertNotNull(closedPaymentResponse.getBody().getRentalDate());
    }


    @SneakyThrows
    @Test
    @DisplayName("Close Payment when film is incorrect then return 400")
    void closePaymentWhenFilmIsIncorrectThenFail() {
        HttpHeaders staffTokenHeaders = paymentTestHelper.savePayment();
        PaymentClosingDto paymentClosingDto = new PaymentClosingDto(paymentTestHelper.getSavedCustomerId(), 0L);
        HttpEntity<PaymentClosingDto> requestEntity = new HttpEntity<>(paymentClosingDto, staffTokenHeaders);

        ResponseEntity<String> closedPaymentResponse = testRestTemplate.exchange(
                "/payments/close",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        BaseResponse failureResponse = objectMapper.readValue(closedPaymentResponse.getBody(), BaseResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, closedPaymentResponse.getStatusCode());
        assertEquals("Payment doesn`t exists, something is wrong", failureResponse.getMessage());
    }
}
