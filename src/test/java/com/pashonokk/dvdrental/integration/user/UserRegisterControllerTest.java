package com.pashonokk.dvdrental.integration.user;

import com.pashonokk.dvdrental.dto.UserCustomerSavingDto;
import com.pashonokk.dvdrental.util.UserBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

//    @Test
//    @DisplayName("Register customer when it exists then throw UserExistsException")
//    void registerUserCustomerWhenUserExistsThrowException() {
//        UserCustomerSavingDto userCustomerSavingDto = ConstructUserCustomerDto();
//
//        ResponseEntity<String> response = testRestTemplate.postForEntity("/users/register/customer",
//                userCustomerSavingDto,
//                String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        assertEquals("Confirming letter was sent to your email", response.getBody());
//    }


    //    @Test
    //    void registerUserStaff() {
    //    }
}