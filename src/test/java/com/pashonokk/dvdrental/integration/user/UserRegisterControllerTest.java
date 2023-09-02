package com.pashonokk.dvdrental.integration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pashonokk.dvdrental.dto.*;
import com.pashonokk.dvdrental.endpoint.BaseResponse;
import com.pashonokk.dvdrental.util.StoreBuilder;
import com.pashonokk.dvdrental.util.UserBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRegisterControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;


    @Test
    @DisplayName("Register customer when it doesn`t exist then save")
    void registerCustomerWhenDoesntExistThenSave() {
        UserCustomerSavingDto userCustomerSavingDto = UserBuilder.constructUserCustomer();

        ResponseEntity<CustomerDto> response = testRestTemplate.postForEntity("/users/register/customer", userCustomerSavingDto, CustomerDto.class);
        String location = String.format("http://localhost:%s/customers/%s", port,
                Objects.requireNonNull(response.getBody()).getId());


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getAddress().getId());
        assertFalse(response.getBody().getIsDeleted());
        assertNotNull(response.getBody().getLastUpdate());
        assertEquals(URI.create(location), response.getHeaders().getLocation());
    }

    @SneakyThrows
    @Test
    @DisplayName("Register customer when it exists then 400")
    void registerUserCustomerWhenUserExistsThenFail() {
        UserCustomerSavingDto userCustomerSavingDto = UserBuilder.constructUserCustomer();
        testRestTemplate.postForEntity("/users/register/customer", userCustomerSavingDto, String.class);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/users/register/customer", userCustomerSavingDto, String.class);
        BaseResponse failureResponse = objectMapper.readValue(response.getBody(), BaseResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(String.format("User with email %s already exists", userCustomerSavingDto.getEmail()), failureResponse.getMessage());
    }


    @Test
    @DisplayName("Register staff when it doesn't exist then save")
    void registerStaffWhenDoesntExistThenSave() {
        UserStaffSavingDto userStaffSavingDto = UserBuilder.constructUserStaff();
        StoreSavingDto store = StoreBuilder.constructStore();
        HttpHeaders headers = constructHttpHeaders();
        Long saveStoreId = saveStore(store, headers);
        userStaffSavingDto.setStoreId(Objects.requireNonNull(saveStoreId));
        HttpEntity<UserStaffSavingDto> staffHttpEntity = new HttpEntity<>(userStaffSavingDto, headers);

        ResponseEntity<StaffDto> createdStaffResponse = testRestTemplate.exchange(
                "/users/register/staff",
                HttpMethod.POST,
                staffHttpEntity,
                StaffDto.class
        );
        String location = String.format("http://localhost:%s/staff/%s", port,
                Objects.requireNonNull(createdStaffResponse.getBody()).getId());


        assertEquals(HttpStatus.CREATED, createdStaffResponse.getStatusCode());
        assertEquals(HttpStatus.CREATED, createdStaffResponse.getStatusCode());
        assertNotNull(createdStaffResponse.getBody().getId());
        assertNotNull(createdStaffResponse.getBody().getAddress().getId());
        assertFalse(createdStaffResponse.getBody().getIsDeleted());
        assertNotNull(createdStaffResponse.getBody().getLastUpdate());
        assertEquals(URI.create(location), createdStaffResponse.getHeaders().getLocation());
    }

    @SneakyThrows
    @Test
    @DisplayName("Register staff when it exists then return 400")
    void registerStaffWhenExistsThenFail() {
        UserStaffSavingDto userStaffSavingDto = UserBuilder.constructUserStaff();
        StoreSavingDto store = StoreBuilder.constructStore();
        HttpHeaders headers = constructHttpHeaders();
        Long saveStoreId = saveStore(store, headers);
        userStaffSavingDto.setStoreId(Objects.requireNonNull(saveStoreId));
        HttpEntity<UserStaffSavingDto> staffHttpEntity = new HttpEntity<>(userStaffSavingDto, headers);
        testRestTemplate.exchange("/users/register/staff", HttpMethod.POST, staffHttpEntity, String.class);

        ResponseEntity<String> failCreatingStaffResponse = testRestTemplate.exchange(
                "/users/register/staff",
                HttpMethod.POST,
                staffHttpEntity,
                String.class
        );
        BaseResponse failureResponse = objectMapper.readValue(failCreatingStaffResponse.getBody(), BaseResponse.class);
        assertEquals(String.format("User with email %s already exists", userStaffSavingDto.getEmail()), failureResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, failCreatingStaffResponse.getStatusCode());
    }

    @Test
    @DisplayName("Register staff when unauthorized then return 403")
    void registerStaffWhenUnauthorizedThenFail() {
        UserStaffSavingDto userStaffSavingDto = UserBuilder.constructUserStaff();
        StoreSavingDto store = StoreBuilder.constructStore();
        HttpHeaders headers = constructHttpHeaders();
        Long saveStoreId = saveStore(store, headers);
        userStaffSavingDto.setStoreId(Objects.requireNonNull(saveStoreId));
        HttpEntity<UserStaffSavingDto> staffHttpEntity = new HttpEntity<>(userStaffSavingDto);

        ResponseEntity<String> createdStaffResponse = testRestTemplate.exchange(
                "/users/register/staff",
                HttpMethod.POST,
                staffHttpEntity,
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, createdStaffResponse.getStatusCode());
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

    private HttpHeaders constructHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authorizeAndGetToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String authorizeAndGetToken() {
        UserAuthorizationDto userAuthorizationDto = new UserAuthorizationDto("pasha.ua@gmail.com", "pashafentisov");
        ResponseEntity<JwtAuthorizationResponse> authorizationResponse = testRestTemplate
                .postForEntity("/authorization", userAuthorizationDto, JwtAuthorizationResponse.class);
        return Objects.requireNonNull(authorizationResponse.getBody()).getAuthorizationToken().getToken();
    }

}