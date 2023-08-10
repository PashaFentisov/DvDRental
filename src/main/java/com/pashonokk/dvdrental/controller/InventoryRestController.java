package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.InventoryDto;
import com.pashonokk.dvdrental.dto.InventorySavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventories")
public class InventoryRestController {
    private final InventoryService inventoryService;

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Long id) {
        InventoryDto inventoryDto = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventoryDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<InventoryDto>> getInventories(@RequestParam(required = false, defaultValue = "0") int page,
                                                                     @RequestParam(required = false, defaultValue = "10") int size,
                                                                     @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 inventories at one time");
        }
        PageResponse<InventoryDto> allInventories = inventoryService.getAllInventories(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allInventories);
    }

    @PostMapping
    public ResponseEntity<InventoryDto> addInventory(@RequestBody InventorySavingDto inventorySavingDto) {
        InventoryDto savedInventory = inventoryService.addInventory(inventorySavingDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedInventory.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedInventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

}
