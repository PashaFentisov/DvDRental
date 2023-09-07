package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.PhoneDto;
import com.pashonokk.dvdrental.service.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/phones")
public class PhoneRestController {
    private final PhoneService phoneService;

    @PatchMapping("/{addressId}/newMain/{newId}")
    public ResponseEntity<PhoneDto> setNewPhoneAsMain(@PathVariable Long addressId, @PathVariable Long newId) {
        PhoneDto newMainPhone = phoneService.setNewPhoneAsMain(addressId, newId);
        return ResponseEntity.ok(newMainPhone);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).DELETE_ACCESS)")
    public ResponseEntity<Object> deletePhone(@PathVariable Long id) {
        phoneService.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}
