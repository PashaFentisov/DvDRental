package com.pashonokk.dvdrental.util;

import com.github.javafaker.Faker;
import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.dto.ContactInfoDto;
import com.pashonokk.dvdrental.dto.UserCustomerSavingDto;
import com.pashonokk.dvdrental.dto.UserStaffSavingDto;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class UserBuilder {
    private final static Faker faker = new Faker(Locale.ENGLISH);
    private final static Random random = new Random();
    public static UserCustomerSavingDto constructUserCustomerDto() {
        return UserCustomerSavingDto.builder()
                .email(String.format("%s%s.ua@gmail.com", faker.name().firstName(), faker.number().digit()))
                .password(UUID.randomUUID().toString().substring(24))
                .contactInfo(constructContactInfo())
                .address(constructAddress())
                .build();
    }

    public static UserStaffSavingDto constructUserStaffDto() {
        return UserStaffSavingDto.builder()
                .email(String.format("%s.ua@gmail.com", UUID.randomUUID().toString().substring(0, 5)))
                .password("1234567890")
                .contactInfo(constructContactInfo())
                .address(constructAddress())
                .pictureUrl("url")
                .storeId(1L)
                .build();
    }

    private static ContactInfoDto constructContactInfo() {
        return ContactInfoDto.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .username(faker.name().username())
                .isActive(faker.bool().bool())
                .build();
    }

    private static AddressSavingDto constructAddress() {
        return AddressSavingDto
                .builder()
                .phone(faker.phoneNumber().cellPhone())
                .district(faker.address().state())
                .street(faker.address().streetAddress())
                .houseNumber(Integer.parseInt(faker.address().buildingNumber()))
                .postalCode(random.nextInt(1001, 99999))
                .cityId(1L)
                .build();
    }
}
