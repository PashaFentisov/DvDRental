package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.AddressDto;
import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressRestController {
    private final AddressService addressService;


    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressByCustomerId(@PathVariable Long id) {
        AddressDto addressByCustomerId = addressService.getAddressWithCityAndCountryByCustomerId(id);
        return ResponseEntity.ok(addressByCustomerId);
    }

    @PostMapping("{id}")
    public ResponseEntity<AddressDto> addAddressToCustomer(@PathVariable Long id, @RequestBody AddressSavingDto addressSavingDto) {
        addressSavingDto.setCustomerId(id);
        AddressDto savedAddress = addressService.addAddressToCustomer(addressSavingDto);
        if (savedAddress == null) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.created(URI.create("localhost:10000/addresses/" + savedAddress.getId())).body(savedAddress);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomersAddress(@PathVariable Long id) {
        addressService.deleteCustomersAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AddressDto> updateSomeFieldsOfAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        addressDto.setId(id);
        AddressDto updatedAddressDto = addressService.updateSomeFieldsOfAddress(addressDto);
        return ResponseEntity.ok(updatedAddressDto);
    }
}
