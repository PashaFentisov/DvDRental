package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.entity.User;
import com.pashonokk.dvdrental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(@ModelAttribute User user) {
        User savedUser = userService.save(user);
        return "http://localhost:10000/confirmEmail/" + savedUser.getToken().getUuid();
    }

}
