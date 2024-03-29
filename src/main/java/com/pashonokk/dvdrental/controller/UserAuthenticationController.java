package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.JwtAuthorizationResponse;
import com.pashonokk.dvdrental.dto.UserAuthorizationDto;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authorization")
public class UserAuthenticationController {
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserAuthenticationController.class);


    @PostMapping
    public ResponseEntity<JwtAuthorizationResponse> authorization(@RequestBody @Valid UserAuthorizationDto userDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        return ResponseEntity.ok(userService.authorize(userDto));
    }
}
