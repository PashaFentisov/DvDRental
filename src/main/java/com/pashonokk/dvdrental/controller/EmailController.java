package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.entity.User;
import com.pashonokk.dvdrental.exception.UserNotFoundException;
import com.pashonokk.dvdrental.service.TokenService;
import com.pashonokk.dvdrental.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final TokenService tokenService;
    private final UserService userService;

    @SneakyThrows
    @GetMapping("/confirmEmail/{token}")
    @ResponseBody
    public String confirmUser(@PathVariable String token) {
        User userByUuid = tokenService.findUserByUuid(token);
        if (userByUuid == null) {
            throw new UserNotFoundException("There isn`t user with such token " + token);
        }
        userService.setUserAsVerified(userByUuid.getId());
        return "You are verified";
    }
}
