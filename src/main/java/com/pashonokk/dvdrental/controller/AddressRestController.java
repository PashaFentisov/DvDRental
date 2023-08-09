package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.AddressDto;
import com.pashonokk.dvdrental.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressRestController {
    private final AddressService addressService;


    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        AddressDto addressById = addressService.getAddressWithCityAndCountryById(id);
        return ResponseEntity.ok(addressById);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        addressDto.setId(id);
        AddressDto updatedAddressDto = addressService.updateSomeFieldsOfAddress(addressDto);
        return ResponseEntity.ok(updatedAddressDto);
    }
}
