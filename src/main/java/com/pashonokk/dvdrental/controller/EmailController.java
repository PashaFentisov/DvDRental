package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.entity.User;
import com.pashonokk.dvdrental.service.TokenService;
import com.pashonokk.dvdrental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final TokenService tokenService;
    private final UserService userService;

    @GetMapping("/confirmEmail/{token}")
    @ResponseBody
    public String confirmUser(@PathVariable String token) {
        User userByUuid = tokenService.findUserByUuid(token);
        if (userByUuid == null) {
            return "Something is not correct";
        }
        userService.setUserAsVerified(userByUuid.getId());
        return "You are verified";
    }
}
