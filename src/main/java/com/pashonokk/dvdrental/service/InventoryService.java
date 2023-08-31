package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.InventoryDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Inventory;
import com.pashonokk.dvdrental.mapper.InventoryMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final PageMapper pageMapper;
    private static final String INVENTORY_ERROR_MESSAGE = "Inventory with id %s doesn't exist";

    @Transactional(readOnly = true)
    public InventoryDto getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(INVENTORY_ERROR_MESSAGE, id)));
        return inventoryMapper.toDto(inventory);
    }

    @Transactional(readOnly = true)
    public PageResponse<InventoryDto> getAllInventories(Pageable pageable) {
        return pageMapper.toPageResponse(inventoryRepository.findAll(pageable).map(inventoryMapper::toDto));
    }

    @Transactional
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException(String.format(INVENTORY_ERROR_MESSAGE, id)));
        inventory.setIsDeleted(true);
    }
}
