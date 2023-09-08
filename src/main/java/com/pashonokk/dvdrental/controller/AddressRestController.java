package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.AddressDto;
import com.pashonokk.dvdrental.dto.PhoneDto;
import com.pashonokk.dvdrental.dto.PhoneSavingDto;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressRestController {
    private final AddressService addressService;
    private final Logger logger = LoggerFactory.getLogger(AddressRestController.class);


    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        AddressDto addressById = addressService.getAddressWithCityAndCountryById(id);
        return ResponseEntity.ok(addressById);
    }

    @PostMapping("{id}/phones")
    public ResponseEntity<PhoneDto> addPhoneToAddress(@PathVariable Long id,
                                                      @RequestBody @Valid PhoneSavingDto phoneSavingDto,
                                                      Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er -> logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        PhoneDto savedPhone = addressService.addPhoneToAddress(id, phoneSavingDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(savedPhone.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedPhone);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        addressDto.setId(id);
        AddressDto updatedAddressDto = addressService.updateSomeFieldsOfAddress(addressDto);
        return ResponseEntity.ok(updatedAddressDto);
    }
}
