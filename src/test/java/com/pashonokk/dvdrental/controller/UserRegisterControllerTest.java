package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.dto.ContactInfoDto;
import com.pashonokk.dvdrental.dto.UserCustomerSavingDto;
import com.pashonokk.dvdrental.entity.Role;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.RoleRepository;
import com.pashonokk.dvdrental.repository.TokenRepository;
import com.pashonokk.dvdrental.repository.UserRepository;
import com.pashonokk.dvdrental.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRegisterControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CityRepository cityRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private TokenRepository tokenRepository;
    private StaffService staffService;
    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;
    @BeforeEach
    void init(){
        Mockito.when(userRepository.findUserIdByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(cityRepository.findByIdWithAddressesAndCountry(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findRoleByName(Mockito.anyString())).thenAnswer((invocation)->{
            return new Role(1L, invocation.getArgument(0), null, null);
        });
    }

    @Test
    void registerUserCustomer() {
        UserCustomerSavingDto userCustomerSavingDto = ConstructUserCustomerDto();

        ResponseEntity<String> response = testRestTemplate.postForEntity("/users/register/customer", userCustomerSavingDto, String.class);

        assertEquals("Confirming letter was sent to your email", response.getBody());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(roleRepository, Mockito.times(1)).findRoleByName("ROLE_CUSTOMER");
    }

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
                .houseNumber(12)
                .postalCode(1234)
                .cityId(1L)
                .build();
        return UserCustomerSavingDto.builder()
                .email("pasha16.ua@gmail.com")
                .password("1234567890")
                .contactInfo(contactInfo)
                .address(address)
                .build();
    }

    @Test
    void registerUserStaff() {
    }
}