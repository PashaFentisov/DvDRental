package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.dto.UserCustomerSavingDto;
import com.pashonokk.dvdrental.dto.UserStaffSavingDto;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRegisterController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserRegisterController.class);

    @PostMapping("/register/customer")
    public ResponseEntity<CustomerDto> registerUserCustomer(@RequestBody @Valid UserCustomerSavingDto userDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        CustomerDto savedCustomer = userService.saveRegisteredCustomerUser(userDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/customers/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCustomer);
    }

    @PostMapping("/register/staff")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).SAVE_STAFF_ACCESS)")
    public ResponseEntity<StaffDto> registerUserStaff(@RequestBody @Valid UserStaffSavingDto userDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        StaffDto savedStaff = userService.saveRegisteredStaffUser(userDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/staff/{id}")
                .buildAndExpand(savedStaff.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedStaff);
    }
}
