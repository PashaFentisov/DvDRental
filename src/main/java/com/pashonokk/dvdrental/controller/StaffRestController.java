package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffRestController {
    private final StaffService staffService;

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
