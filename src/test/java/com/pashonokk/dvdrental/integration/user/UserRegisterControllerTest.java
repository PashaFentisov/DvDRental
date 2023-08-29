package com.pashonokk.dvdrental.integration.user;

import com.pashonokk.dvdrental.dto.*;
import com.pashonokk.dvdrental.util.StoreBuilder;
import com.pashonokk.dvdrental.util.UserBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRegisterControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    @DisplayName("Register customer when it doesn`t exist then save")
    void registerCustomerWhenDoesntExistThenSave() {
        UserCustomerSavingDto userCustomerSavingDto = UserBuilder.constructUserCustomerDto();

        ResponseEntity<String> response = testRestTemplate.postForEntity("/users/register/customer", userCustomerSavingDto, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Confirming letter was sent to your email", response.getBody());
    }

    @Test
    @DisplayName("Register customer when it exists then 400")
    void registerUserCustomerWhenUserExistsThenFail() {
        UserCustomerSavingDto userCustomerSavingDto = UserBuilder.constructUserCustomerDto();
        testRestTemplate.postForEntity("/users/register/customer", userCustomerSavingDto, String.class);

        ResponseEntity<String> response = testRestTemplate.postForEntity("/users/register/customer", userCustomerSavingDto, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private String authorizeAndGetToken() {
        UserAuthorizationDto userAuthorizationDto = new UserAuthorizationDto("pasha.ua@gmail.com", "pashafentisov");
        ResponseEntity<JwtAuthorizationResponse> authorizationResponse = testRestTemplate
                .postForEntity("/authorization", userAuthorizationDto, JwtAuthorizationResponse.class);
        return Objects.requireNonNull(authorizationResponse.getBody()).getAuthorizationToken().getToken();
    }


    @Test
    @DisplayName("Register staff when it doesn't exist then save")
    void registerStaffWhenDoesntExistThenSave() {
        UserStaffSavingDto userStaffSavingDto = UserBuilder.constructUserStaffDto();
        StoreSavingDto store = StoreBuilder.constructStoreSavingDto();
        String jwtToken = authorizeAndGetToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StoreSavingDto> requestEntity = new HttpEntity<>(store, headers);
        ResponseEntity<StoreDto> createdStoreResponse = testRestTemplate.exchange(
                "/stores",
                HttpMethod.POST,
                requestEntity,
                StoreDto.class
        );
        userStaffSavingDto.setStoreId(Objects.requireNonNull(createdStoreResponse.getBody()).getId());
        HttpEntity<UserStaffSavingDto> staffHttpEntity = new HttpEntity<>(userStaffSavingDto, headers);

        ResponseEntity<String> createdStaffResponse = testRestTemplate.exchange(
                "/users/register/staff",
                HttpMethod.POST,
                staffHttpEntity,
                String.class
        );

        assertEquals(HttpStatus.OK, createdStaffResponse.getStatusCode());
        assertEquals("Confirming letter was sent to your email", createdStaffResponse.getBody());
    }

    @Test
    @DisplayName("Register staff when it exists then return 400")
    void registerStaffWhenExistsThenFail() {
        UserStaffSavingDto userStaffSavingDto = UserBuilder.constructUserStaffDto();
        StoreSavingDto store = StoreBuilder.constructStoreSavingDto();
        String jwtToken = authorizeAndGetToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StoreSavingDto> requestEntity = new HttpEntity<>(store, headers);
        ResponseEntity<StoreDto> createdStoreResponse = testRestTemplate.exchange(
                "/stores",
                HttpMethod.POST,
                requestEntity,
                StoreDto.class
        );
        userStaffSavingDto.setStoreId(Objects.requireNonNull(createdStoreResponse.getBody()).getId());
        HttpEntity<UserStaffSavingDto> staffHttpEntity = new HttpEntity<>(userStaffSavingDto, headers);
        testRestTemplate.exchange("/users/register/staff", HttpMethod.POST, staffHttpEntity, String.class);

        ResponseEntity<String> failCreatingStaffResponse = testRestTemplate.exchange(
                "/users/register/staff",
                HttpMethod.POST,
                staffHttpEntity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, failCreatingStaffResponse.getStatusCode());
    }

    @Test
    @DisplayName("Register staff when unauthorized then return 403")
    void registerStaffWhenUnauthorizedThenFail() {
        UserStaffSavingDto userStaffSavingDto = UserBuilder.constructUserStaffDto();
        StoreSavingDto store = StoreBuilder.constructStoreSavingDto();
        String jwtToken = authorizeAndGetToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StoreSavingDto> requestEntity = new HttpEntity<>(store, headers);
        ResponseEntity<StoreDto> createdStoreResponse = testRestTemplate.exchange(
                "/stores",
                HttpMethod.POST,
                requestEntity,
                StoreDto.class
        );
        userStaffSavingDto.setStoreId(Objects.requireNonNull(createdStoreResponse.getBody()).getId());
        HttpEntity<UserStaffSavingDto> staffHttpEntity = new HttpEntity<>(userStaffSavingDto);

        ResponseEntity<String> createdStaffResponse = testRestTemplate.exchange(
                "/users/register/staff",
                HttpMethod.POST,
                staffHttpEntity,
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, createdStaffResponse.getStatusCode());
    }

}