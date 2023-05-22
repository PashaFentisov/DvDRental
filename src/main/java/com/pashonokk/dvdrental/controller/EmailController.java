package com.pashonokk.dvdrental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailController {

    @GetMapping("/confirmEmail/{token}")
    @ResponseBody
    public String confirmUser(@PathVariable String token){


        return "You confirmed your email";
    }
}
