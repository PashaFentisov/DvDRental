package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.service.NotificationService;
import com.pashonokk.dvdrental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping("/confirmEmail/{token}")
    @ResponseBody
    public String confirmUser(@PathVariable String token) {
        userService.confirmUserEmail(token);
        return "You are verified";
    }

    @GetMapping("/sendConfirmingLetter/{email}")
    @ResponseBody
    public String sendConfirmingLetterToEmail(@PathVariable String email) {
        notificationService.send(email);
        return "Confirming letter was sent to your email";
    }
}
