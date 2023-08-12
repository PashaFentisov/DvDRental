package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.FilmDto;
import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.dto.StoreDto;
import com.pashonokk.dvdrental.dto.StoreSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreRestController {
    private final StoreService storeService;

    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable Long id) {
        StoreDto storeDto = storeService.getStoreById(id);
        return ResponseEntity.ok(storeDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<StoreDto>> getStores(@RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size,
                                                          @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 stores at one time");
        }
        PageResponse<StoreDto> allStores = storeService.getAllStores(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allStores);
    }

    @GetMapping("/{id}/staff")
    public ResponseEntity<List<StaffDto>> getStaffByStoreId(@PathVariable Long id) {
        List<StaffDto> listOfStaff = storeService.getStaffByStoreId(id);
        return ResponseEntity.ok(listOfStaff);
    }

    @GetMapping("/{id}/films")
    public ResponseEntity<List<FilmDto>> getStoreFilms(@PathVariable Long id) {
        List<FilmDto> storeFilms = storeService.getStoreFilms(id);
        return ResponseEntity.ok(storeFilms);
    }

    @PostMapping
    public ResponseEntity<StoreDto> addStore(@RequestBody StoreSavingDto storeSavingDto) {
        StoreDto savedStore = storeService.addStore(storeSavingDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStore.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedStore);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}
