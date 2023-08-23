package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.UserCustomerSavingDto;
import com.pashonokk.dvdrental.dto.UserStaffSavingDto;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRegisterController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserRegisterController.class);

    @PostMapping("/register/customer")
    public String registerUserCustomer(@RequestBody @Valid UserCustomerSavingDto userDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        userService.saveRegisteredCustomerUser(userDto);
        return "Confirming letter was sent to your email";
    }

    @PostMapping("/register/staff")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).SAVE_STAFF_ACCESS)")
    public String registerUserStaff(@RequestBody @Valid UserStaffSavingDto userDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        userService.saveRegisteredStaffUser(userDto);
        return "Confirming letter was sent to your email";
    }
}
