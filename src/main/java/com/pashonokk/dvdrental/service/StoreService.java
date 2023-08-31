package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.FilmDto;
import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.dto.StoreDto;
import com.pashonokk.dvdrental.dto.StoreSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Store;
import com.pashonokk.dvdrental.mapper.*;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final CityRepository cityRepository;
    private final StoreMapper storeMapper;
    private final FilmMapper filmMapper;
    private final StaffMapper staffMapper;
    private final PageMapper pageMapper;
    private final StoreSavingMapper storeSavingMapper;
    private static final String STORE_ERROR_MESSAGE = "Store with id %s doesn't exist";

    @Transactional(readOnly = true)
    public StoreDto getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(STORE_ERROR_MESSAGE, id)));
        return storeMapper.toDto(store);
    }

    @Transactional(readOnly = true)
    public PageResponse<StoreDto> getAllStores(Pageable pageable) {
        return pageMapper.toPageResponse(storeRepository.findAll(pageable).map(storeMapper::toDto));
    }

    @Transactional(readOnly = true)
    public List<StaffDto> getStaffByStoreId(Long id) {
        return storeRepository.findStoreById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(STORE_ERROR_MESSAGE, id))).getStaff()
                .stream().map(staffMapper::toDto).toList();
    }
    @Transactional(readOnly = true)
    public List<FilmDto> getStoreFilms(Long id) {
        Store store = storeRepository.readStoreById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(STORE_ERROR_MESSAGE, id)));
        return store.getInventories().stream().map(inventory -> filmMapper.toDto(inventory.getFilm())).toList();
    }

    @Transactional
    public StoreDto addStore(StoreSavingDto storeSavingDto) {
        Store store = storeSavingMapper.toEntity(storeSavingDto);
        store.setLastUpdate(OffsetDateTime.now());
        store.setIsDeleted(false);
        Address address = store.getAddress();
        address.setLastUpdate(OffsetDateTime.now());
        address.setIsDeleted(false);
        store.addAddress(address);
        cityRepository.findByIdWithAddressesAndCountry(storeSavingDto.getAddressSavingDto().getCityId()).ifPresent(city->city.addAddress(address));
        Store savedStore = storeRepository.save(store);
        return storeMapper.toDto(savedStore);
    }

    @Transactional
    public void deleteStore(Long id) {
        Store store = storeRepository.getStoreById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(STORE_ERROR_MESSAGE, id)));
        store.setIsDeleted(true);
        store.getAddress().setIsDeleted(true);

    }
}
