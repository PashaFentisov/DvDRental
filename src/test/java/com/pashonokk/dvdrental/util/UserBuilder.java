package com.pashonokk.dvdrental.util;

import com.github.javafaker.Faker;
import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.dto.ContactInfoDto;
import com.pashonokk.dvdrental.dto.UserCustomerSavingDto;
import com.pashonokk.dvdrental.dto.UserStaffSavingDto;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class UserBuilder {
    private final static Faker FAKER = new Faker(Locale.ENGLISH);
    private final static Random RANDOM = new Random();
    public static UserCustomerSavingDto constructUserCustomer() {
        return UserCustomerSavingDto.builder()
                .email(String.format("%s%s.ua@gmail.com", FAKER.name().firstName(), FAKER.number().digit()))
                .password(UUID.randomUUID().toString().substring(24))
                .contactInfo(constructContactInfo())
                .address(constructAddress())
                .build();
    }

    public static UserStaffSavingDto constructUserStaff() {
        return UserStaffSavingDto.builder()
                .email(String.format("%s%s.ua@gmail.com", FAKER.name().firstName(), FAKER.number().digit()))
                .password(UUID.randomUUID().toString().substring(24))
                .contactInfo(constructContactInfo())
                .address(constructAddress())
                .pictureUrl(FAKER.internet().url())
                .build();
    }

    public static ContactInfoDto constructContactInfo() {
        return ContactInfoDto.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .username(FAKER.name().username())
                .isActive(FAKER.bool().bool())
                .build();
    }

    public static AddressSavingDto constructAddress() {
        return AddressSavingDto
                .builder()
                .phone(FAKER.phoneNumber().cellPhone())
                .district(FAKER.address().state())
                .street(FAKER.address().streetAddress())
                .houseNumber(Integer.parseInt(FAKER.address().buildingNumber()))
                .postalCode(RANDOM.nextInt(1001, 99999))
                .cityId(1L)
                .isDeleted(false)
                .lastUpdate(OffsetDateTime.now())
                .build();
    }
}
