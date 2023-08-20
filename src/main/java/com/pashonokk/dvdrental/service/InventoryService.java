package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.InventoryDto;
import com.pashonokk.dvdrental.dto.InventorySavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Film;
import com.pashonokk.dvdrental.entity.Inventory;
import com.pashonokk.dvdrental.entity.Store;
import com.pashonokk.dvdrental.mapper.InventoryMapper;
import com.pashonokk.dvdrental.mapper.InventorySavingMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.repository.FilmRepository;
import com.pashonokk.dvdrental.repository.InventoryRepository;
import com.pashonokk.dvdrental.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final FilmRepository filmRepository;
    private final StoreRepository storeRepository;
    private final InventoryMapper inventoryMapper;
    private final InventorySavingMapper inventorySavingMapper;
    private final PageMapper pageMapper;
    private static final String INVENTORY_ERROR_MESSAGE = "Inventory with id %s doesn't exist";
    private static final String STORE_ERROR_MESSAGE = "Store with id %s doesn't exist";
    private static final String FILM_ERROR_MESSAGE = "Film with id %s doesn't exist";

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
    public InventoryDto addInventory(InventorySavingDto inventorySavingDto) {
        Inventory inventory = inventorySavingMapper.toEntity(inventorySavingDto);
        inventory.setLastUpdate(OffsetDateTime.now());
        Store store = storeRepository.getStoreById(inventorySavingDto.getStoreId())
                .orElseThrow(()->new EntityNotFoundException(String.format(STORE_ERROR_MESSAGE,inventorySavingDto.getStoreId())));
        Film film = filmRepository.findByIdWithCategoriesAndLanguages(inventorySavingDto.getFilmId())
                .orElseThrow(()->new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE,inventorySavingDto.getFilmId())));
        inventory.addFilm(film);
        inventory.addStore(store);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.toDto(savedInventory);
    }

    @Transactional
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException(String.format(INVENTORY_ERROR_MESSAGE, id)));
        inventory.setIsDeleted(true);
    }
}
