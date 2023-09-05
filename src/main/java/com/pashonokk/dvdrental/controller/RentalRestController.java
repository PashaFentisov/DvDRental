package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.RentalDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalRestController {
    private final RentalService rentalService;

    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long id) {
        RentalDto rentalDto = rentalService.getRentalById(id);
        return ResponseEntity.ok(rentalDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<RentalDto>> getRentals(@RequestParam(required = false, defaultValue = "0") int page,
                                                              @RequestParam(required = false, defaultValue = "10") int size,
                                                              @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 rentals at one time");
        }
        PageResponse<RentalDto> allRentals = rentalService.getAllRentals(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allRentals);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).DELETE_ACCESS)")
    public ResponseEntity<Object> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }
}
