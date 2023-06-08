package com.pashonokk.dvdrental.apiInformation.controller;

import com.pashonokk.dvdrental.apiInformation.entity.ApiInfo;
import com.pashonokk.dvdrental.apiInformation.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final ApiService apiService;

    @GetMapping("/api")
    public ApiInfo getApiInf() {
        return apiService.getApiInfo();
    }
}
