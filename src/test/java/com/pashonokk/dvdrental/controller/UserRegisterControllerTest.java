package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.dto.ContactInfoDto;
import com.pashonokk.dvdrental.dto.UserCustomerSavingDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

//TODO: 1.Divide by packages: unit(unit --> user --> UserServiceTest), integration (--> user --> UserRegisterControllerTest),
// 2. Add negative tests (if user exist)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRegisterControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;//TODO: replace with WebTestClient

    @Test
    @DisplayName("Register customer")
    void registerUserCustomer() {
        UserCustomerSavingDto userCustomerSavingDto = ConstructUserCustomerDto();

        ResponseEntity<String> response = testRestTemplate.postForEntity("/users/register/customer",
                                                                         userCustomerSavingDto,
                                                                         String.class);
        assertEquals(HttpStatus.OK,
                     response.getStatusCode());

        assertEquals("Confirming letter was sent to your email",
                     response.getBody());
    }

    //TODO extract to helper class
    private static UserCustomerSavingDto ConstructUserCustomerDto() {
        ContactInfoDto contactInfo = ContactInfoDto.builder()
                                                   .firstName("Pasha")
                                                   .lastName("Second")
                                                   .username("pashok")
                                                   .isActive(true)
                                                   .build();
        AddressSavingDto address = AddressSavingDto
                .builder()
                .phone("0986422332")
                .district("first district")
                .street("second street")
                .houseNumber(11)
                .postalCode(1234)
                .cityId(1L)
                .build();
        return UserCustomerSavingDto.builder()
                                    .email(String.format("%s.ua@gmail.com",
                                                         UUID.randomUUID()
                                                             .toString()
                                                             .substring(0,
                                                                        5)))
                                    .password("1234567890")
                                    .contactInfo(contactInfo)
                                    .address(address)
                                    .build();
    }

    //    @Test
    //    void registerUserStaff() {
    //    }
}