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
@RequestMapping("/register")
public class UserRegisterController {
    private final UserService userService;

    @GetMapping
    public String register(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            return "register";
        }
        String email = userService.saveRegisteredUser(userDto);
        return "redirect:/sendConfirmingLetter/" + email;
    }
}
