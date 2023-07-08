package com.pashonokk.dvdrental.controller;

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

    @PostMapping("{id}")
    public RedirectView addAddressToCustomer(@PathVariable Long id, @RequestBody AddressSavingDto addressSavingDto) {
        addressSavingDto.setCustomerId(id);
        addressService.addAddressToCustomer(addressSavingDto);  //todo перевіряти якщо адреса вже є то не робим нічо есле сетим
        return new RedirectView("/customers");
    }


//    @DeleteMapping("/{id}")
//    public RedirectView deleteCustomersAddress(@PathVariable Long id) {
//        categoryService.deleteCategoryById(id);
//        return new RedirectView(REDIRECT_TO_ALL);
//    }
//
//    @PutMapping("/{id}")
//    public RedirectView updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
//        categoryDto.setId(id);
//        categoryService.addCategory(categoryDto);
//        return new RedirectView(REDIRECT_TO_ALL);
//    }
//
//    @PatchMapping("/{id}")
//    public RedirectView updateSomeFieldsOfCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
//        categoryDto.setId(id);
//        categoryService.partialUpdateCategory(categoryDto);
//        return new RedirectView(REDIRECT_TO_ALL);
//    }

    //delete
    //put
    //patch
    //get address by customer id  in customer controller
}
