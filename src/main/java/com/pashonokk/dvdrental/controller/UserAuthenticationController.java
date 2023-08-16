package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.UserDto;
import com.pashonokk.dvdrental.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserAuthenticationController {
    private final UserService userService;

    @GetMapping
    public String auth(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth";
    }

    @PostMapping
    @ResponseBody
    public String auth(@Valid @ModelAttribute UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            return "auth";
        }
        return userService.signIn(userDto);
    }
}
