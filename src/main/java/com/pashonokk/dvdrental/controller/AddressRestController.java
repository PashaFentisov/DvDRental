package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.AddressDto;
import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressRestController {
    private final AddressService addressService;
    public static final String REDIRECT_TO_ALL_CUSTOMERS = "/customers";


    @GetMapping("/{id}")
    public AddressDto getAddressByCustomerId(@PathVariable Long id) {
        return addressService.getAddressByCustomerId(id);
    }

    @PostMapping("{id}")
    public RedirectView addAddressToCustomer(@PathVariable Long id, @RequestBody AddressSavingDto addressSavingDto) {
        addressSavingDto.setCustomerId(id);
        addressService.addAddressToCustomer(addressSavingDto);
        return new RedirectView(REDIRECT_TO_ALL_CUSTOMERS);
    }


    @DeleteMapping("/{id}")
    public RedirectView deleteCustomersAddress(@PathVariable Long id) {
        addressService.deleteCustomersAddress(id);
        return new RedirectView(REDIRECT_TO_ALL_CUSTOMERS);
    }

    @PutMapping("/{id}")
    public RedirectView updateCategory(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        addressDto.setId(id);
        addressService.updateAddress(addressDto);
        return new RedirectView(REDIRECT_TO_ALL_CUSTOMERS);
    }

    @PatchMapping("/{id}")
    public RedirectView updateSomeFieldsOfAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        addressDto.setId(id);
        addressService.updateSomeFieldsOfAddress(addressDto);
        return new RedirectView(REDIRECT_TO_ALL_CUSTOMERS);
    }
}
