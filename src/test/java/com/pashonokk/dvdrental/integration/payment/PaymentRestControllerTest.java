package com.pashonokk.dvdrental.integration.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pashonokk.dvdrental.dto.*;
import com.pashonokk.dvdrental.endpoint.BaseResponse;
import com.pashonokk.dvdrental.util.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentRestControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @LocalServerPort
    private int port;
    @Value("${dvd.oneday.price}")
    private BigDecimal price;

    private Long savedFilmId;
    private Long savedStoreId;
    private Long savedCustomerId;

    HttpHeaders init() {
        HttpHeaders headersWithToken = constructAdminHttpHeaders();
        StoreSavingDto store = StoreBuilder.constructStore();
        savedStoreId = saveStore(store, headersWithToken);

        LanguageDto language = FilmBuilder.constructLanguage();
        Long savedLanguageId = saveLanguage(language, headersWithToken);

        FilmSavingDto film = FilmBuilder.constructFilm();
        film.setLanguagesIds(Set.of(savedLanguageId));
        savedFilmId = saveFilm(film, headersWithToken);

        InventorySavingDto inventory = InventoryBuilder.constructInventorySavingDto(savedFilmId, savedStoreId);
        saveInventory(inventory, headersWithToken);

        UserCustomerSavingDto customer = UserBuilder.constructUserCustomer();
        savedCustomerId = saveCustomer(customer);

        UserStaffSavingDto staff = UserBuilder.constructUserStaff();
        staff.setStoreId(savedStoreId);
        saveStaff(staff, headersWithToken);

        return constructStaffHttpHeaders(staff);
    }


    @Test
    @DisplayName("Create Payment when everything is correct then save")
    void createPaymentWhenCorrectThenSave() {
        HttpHeaders staffTokenHeaders = init();
        PaymentSavingDto payment = PaymentBuilder.constructPayment(savedFilmId, savedStoreId, savedCustomerId);
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
        assertEquals(price.multiply(BigDecimal.valueOf(payment.getRentalDays())), savedPaymentResponse.getBody().getAmount());
        assertEquals(URI.create(location), savedPaymentResponse.getHeaders().getLocation());
    }


    @Test
    @DisplayName("Create Payment when unauthorized then 403")
    void createPaymentWhenUnauthorizedThenFail() {
        PaymentSavingDto payment = PaymentBuilder.constructPayment(savedFilmId, savedStoreId, savedCustomerId);
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
        HttpHeaders staffTokenHeaders = init();
        PaymentSavingDto payment = PaymentBuilder.constructPayment(savedFilmId, savedStoreId, savedCustomerId+1);
        HttpEntity<PaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        ResponseEntity<String> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        BaseResponse failureResponse = objectMapper.readValue(savedPaymentResponse.getBody(), BaseResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, savedPaymentResponse.getStatusCode());
        assertEquals(String.format("Customer with id %s doesn't exist", savedCustomerId+1), failureResponse.getMessage());
    }


    @SneakyThrows
    @Test
    @DisplayName("Create Payment when inventory doesnt exist then return 400")
    void createPaymentWhenInventoryDoesntExistThenFail() {
        HttpHeaders staffTokenHeaders = init();
        PaymentSavingDto payment = PaymentBuilder.constructPayment(savedFilmId+1, savedStoreId+1, savedCustomerId);
        HttpEntity<PaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        ResponseEntity<String> savedPaymentResponse = testRestTemplate.exchange(
                "/payments",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        BaseResponse failureResponse = objectMapper.readValue(savedPaymentResponse.getBody(), BaseResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, savedPaymentResponse.getStatusCode());
        assertEquals(String.format("Inventory with film id %s and store id %s doesn't exist", savedFilmId+1, savedStoreId+1), failureResponse.getMessage());
    }
    private HttpHeaders constructAdminHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authorizeAsAdminAndGetToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpHeaders constructStaffHttpHeaders(UserStaffSavingDto staff) {
        HttpHeaders staffTokenHeaders = new HttpHeaders();
        staffTokenHeaders.set("Authorization", "Bearer " + authorizeAsStaffAndGetToken(staff.getEmail(), staff.getPassword()));
        staffTokenHeaders.setContentType(MediaType.APPLICATION_JSON);
        return staffTokenHeaders;
    }

    private String authorizeAsAdminAndGetToken() {
        UserAuthorizationDto userAuthorizationDto = new UserAuthorizationDto("pasha.ua@gmail.com", "pashafentisov");
        ResponseEntity<JwtAuthorizationResponse> authorizationResponse = testRestTemplate
                .postForEntity("/authorization", userAuthorizationDto, JwtAuthorizationResponse.class);
        return Objects.requireNonNull(authorizationResponse.getBody()).getAuthorizationToken().getToken();
    }

    private String authorizeAsStaffAndGetToken(String email, String password) {
        UserAuthorizationDto userAuthorizationDto = new UserAuthorizationDto(email, password);
        ResponseEntity<JwtAuthorizationResponse> authorizationResponse = testRestTemplate
                .postForEntity("/authorization", userAuthorizationDto, JwtAuthorizationResponse.class);
        return Objects.requireNonNull(authorizationResponse.getBody()).getAuthorizationToken().getToken();
    }

    private Long saveCustomer(UserCustomerSavingDto customer) {
        ResponseEntity<CustomerDto> response = testRestTemplate.postForEntity("/users/register/customer", customer, CustomerDto.class);
        return Objects.requireNonNull(response.getBody()).getId();
    }

    private void saveInventory(InventorySavingDto inventory, HttpHeaders headers) {
        HttpEntity<InventorySavingDto> requestEntity = new HttpEntity<>(inventory, headers);
        testRestTemplate.exchange(
                "/inventories",
                HttpMethod.POST,
                requestEntity,
                InventoryDto.class
        );
    }

    private void saveStaff(UserStaffSavingDto staff, HttpHeaders headers) {
        HttpEntity<UserStaffSavingDto> requestEntity = new HttpEntity<>(staff, headers);
        testRestTemplate.exchange(
                "/users/register/staff",
                HttpMethod.POST,
                requestEntity,
                StaffDto.class);
    }

    private Long saveFilm(FilmSavingDto film, HttpHeaders headers) {
        HttpEntity<FilmSavingDto> requestEntity = new HttpEntity<>(film, headers);
        ResponseEntity<FilmDto> createdFilmResponse = testRestTemplate.exchange(
                "/films",
                HttpMethod.POST,
                requestEntity,
                FilmDto.class
        );
        return Objects.requireNonNull(createdFilmResponse.getBody()).getId();
    }

    private Long saveLanguage(LanguageDto language, HttpHeaders headers) {
        HttpEntity<LanguageDto> requestEntity = new HttpEntity<>(language, headers);
        ResponseEntity<LanguageDto> createdLanguageResponse = testRestTemplate.exchange(
                "/languages",
                HttpMethod.POST,
                requestEntity,
                LanguageDto.class
        );
        return Objects.requireNonNull(createdLanguageResponse.getBody()).getId();
    }

    private Long saveStore(StoreSavingDto store, HttpHeaders headers) {
        HttpEntity<StoreSavingDto> requestEntity = new HttpEntity<>(store, headers);
        ResponseEntity<StoreDto> createdStoreResponse = testRestTemplate.exchange(
                "/stores",
                HttpMethod.POST,
                requestEntity,
                StoreDto.class
        );
        return Objects.requireNonNull(createdStoreResponse.getBody()).getId();
    }
}
