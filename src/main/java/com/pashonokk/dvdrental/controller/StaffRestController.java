package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.dto.StaffSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffRestController {
    private final StaffService staffService;
    private final Logger logger = LoggerFactory.getLogger(StaffRestController.class);

    @GetMapping("/{id}")
    public ResponseEntity<StaffDto> getStaffById(@PathVariable Long id) {
        StaffDto staffDto = staffService.getStaffById(id);
        return ResponseEntity.ok(staffDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<StaffDto>> getStaff(@RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "10") int size,
                                                            @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 staff at one time");
        }
        PageResponse<StaffDto> allStaff = staffService.getAllStaff(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allStaff);
    }

    @PostMapping
    public ResponseEntity<StaffDto> addStaff(@RequestBody @Valid StaffSavingDto staffSavingDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        StaffDto savedStaff = staffService.addStaff(staffSavingDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStaff.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedStaff);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StaffDto> updateStaff(@PathVariable Long id, @RequestBody StaffDto staffDto) {
        staffDto.setId(id);
        StaffDto updatedStaffDto = staffService.updateSomeFieldsOfStaff(staffDto);
        return ResponseEntity.ok(updatedStaffDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).DELETE_ACCESS)")
    public ResponseEntity<Object> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
