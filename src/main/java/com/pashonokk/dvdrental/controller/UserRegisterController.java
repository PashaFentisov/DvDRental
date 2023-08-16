package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.UserDto;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRegisterController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserRegisterController.class);

    @PostMapping
    public String register(@RequestBody @Valid UserDto userDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        userService.saveRegisteredUser(userDto);
        return "Confirming letter was sent to your email";
    }
}
