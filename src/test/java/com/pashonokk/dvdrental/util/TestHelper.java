package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.dto.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Getter
public class TestHelper {
    private final TestRestTemplate testRestTemplate;
    private Long savedFilmId;
    private Long savedStoreId;
    private Long savedCustomerId;
    private PaymentDto savedPayment;

    public HttpHeaders preparePaymentSaving() {
        HttpHeaders headersWithToken = constructAdminHttpHeaders();
        StoreSavingDto store = StoreBuilder.constructStore();
        savedStoreId = saveStore(store, headersWithToken).getId();

        LanguageDto language = FilmBuilder.constructLanguage();
        Long savedLanguageId = saveLanguage(language, headersWithToken);

        FilmSavingDto film = FilmBuilder.constructFilm();
        film.setLanguagesIds(Set.of(savedLanguageId));
        film.setStoreId(savedStoreId);
        savedFilmId = saveFilm(film, headersWithToken);

        UserCustomerSavingDto customer = UserBuilder.constructUserCustomer();
        savedCustomerId = saveCustomer(customer);

        UserStaffSavingDto staff = UserBuilder.constructUserStaff();
        staff.setStoreId(savedStoreId);
        saveStaff(staff, headersWithToken);

        return constructStaffHttpHeaders(staff);
    }

    public HttpHeaders savePayment() {
        HttpHeaders staffTokenHeaders = preparePaymentSaving();
        PaymentSavingDto payment = PaymentBuilder.constructPayment(savedFilmId, savedCustomerId);
        HttpEntity<PaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        savedPayment = testRestTemplate.exchange("/payments", HttpMethod.POST, requestEntity, PaymentDto.class).getBody();
        return staffTokenHeaders;
    }

    public HttpHeaders saveExpiredPayment() {
        HttpHeaders staffTokenHeaders = preparePaymentSaving();
        PaymentSavingDto payment = PaymentBuilder.constructPayment(savedFilmId, savedCustomerId);
        payment.setRentalDays(-10);
        HttpEntity<PaymentSavingDto> requestEntity = new HttpEntity<>(payment, staffTokenHeaders);

        savedPayment = testRestTemplate.exchange("/payments", HttpMethod.POST, requestEntity, PaymentDto.class).getBody();
        return staffTokenHeaders;
    }

    public HttpHeaders constructAdminHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authorizeAsAdminAndGetToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpHeaders constructStaffHttpHeaders(UserStaffSavingDto staff) {
        HttpHeaders staffTokenHeaders = new HttpHeaders();
        staffTokenHeaders.set("Authorization", "Bearer " + authorizeAsStaffAndGetToken(staff.getEmail(), staff.getPassword()));
        staffTokenHeaders.setContentType(MediaType.APPLICATION_JSON);
        return staffTokenHeaders;
    }

    public String authorizeAsAdminAndGetToken() {
        UserAuthorizationDto userAuthorizationDto = new UserAuthorizationDto("pasha.ua@gmail.com", "pashafentisov");
        ResponseEntity<JwtAuthorizationResponse> authorizationResponse = testRestTemplate
                .postForEntity("/authorization", userAuthorizationDto, JwtAuthorizationResponse.class);
        return Objects.requireNonNull(authorizationResponse.getBody()).getAuthorizationToken().getToken();
    }

    public String authorizeAsStaffAndGetToken(String email, String password) {
        UserAuthorizationDto userAuthorizationDto = new UserAuthorizationDto(email, password);
        ResponseEntity<JwtAuthorizationResponse> authorizationResponse = testRestTemplate
                .postForEntity("/authorization", userAuthorizationDto, JwtAuthorizationResponse.class);
        return Objects.requireNonNull(authorizationResponse.getBody()).getAuthorizationToken().getToken();
    }

    public Long saveCustomer(UserCustomerSavingDto customer) {
        ResponseEntity<CustomerDto> response = testRestTemplate.postForEntity("/users/register/customer", customer, CustomerDto.class);
        return Objects.requireNonNull(response.getBody()).getId();
    }

    public void saveStaff(UserStaffSavingDto staff, HttpHeaders headers) {
        HttpEntity<UserStaffSavingDto> requestEntity = new HttpEntity<>(staff, headers);
        testRestTemplate.exchange(
                "/users/register/staff",
                HttpMethod.POST,
                requestEntity,
                StaffDto.class);
    }

    public Long saveFilm(FilmSavingDto film, HttpHeaders headers) {
        HttpEntity<FilmSavingDto> requestEntity = new HttpEntity<>(film, headers);
        ResponseEntity<FilmDto> createdFilmResponse = testRestTemplate.exchange(
                "/films",
                HttpMethod.POST,
                requestEntity,
                FilmDto.class
        );
        return Objects.requireNonNull(createdFilmResponse.getBody()).getId();
    }

    public Long saveLanguage(LanguageDto language, HttpHeaders headers) {
        HttpEntity<LanguageDto> requestEntity = new HttpEntity<>(language, headers);
        ResponseEntity<LanguageDto> createdLanguageResponse = testRestTemplate.exchange(
                "/languages",
                HttpMethod.POST,
                requestEntity,
                LanguageDto.class
        );
        return Objects.requireNonNull(createdLanguageResponse.getBody()).getId();
    }

    public StoreDto saveStore(StoreSavingDto store, HttpHeaders headers) {
        HttpEntity<StoreSavingDto> requestEntity = new HttpEntity<>(store, headers);
        ResponseEntity<StoreDto> createdStoreResponse = testRestTemplate.exchange(
                "/stores",
                HttpMethod.POST,
                requestEntity,
                StoreDto.class
        );
        return createdStoreResponse.getBody();
    }
}
