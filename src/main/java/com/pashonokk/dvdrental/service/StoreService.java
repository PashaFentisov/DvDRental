package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.StoreDto;
import com.pashonokk.dvdrental.dto.StoreSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Store;
import com.pashonokk.dvdrental.exception.StoreWithoutAddressException;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.mapper.StoreMapper;
import com.pashonokk.dvdrental.mapper.StoreSavingMapper;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final CityRepository cityRepository;
    private final StoreMapper storeMapper;
    private final PageMapper pageMapper;
    private final StoreSavingMapper storeSavingMapper;
    private static final String STORE_ERROR_MESSAGE = "Store with id %s doesn't exist";

    @Transactional(readOnly = true)
    public StoreDto getStoreById(Long id) {
        Store store = storeRepository.findByIdWithFullAddress(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(STORE_ERROR_MESSAGE, id)));
        return storeMapper.toDto(store);
    }

    @Transactional(readOnly = true)
    public PageResponse<StoreDto> getAllStores(Pageable pageable) {
        return pageMapper.toPageResponse(storeRepository.findAll(pageable).map(storeMapper::toDto));
    }

    @Transactional
    public StoreDto addStore(StoreSavingDto storeSavingDto) {
        Store store = storeSavingMapper.toEntity(storeSavingDto);
        Address address = store.getAddress();
        if(address==null){
            throw new StoreWithoutAddressException("Store cannot exist without an address");
        }
        store.addAddress(address);
        cityRepository.findByIdWithAddressesAndCountry(storeSavingDto.getAddressSavingDto().getCityId()).ifPresent(city->city.addAddress(address));
        Store savedStore = storeRepository.save(store);
        return storeMapper.toDto(savedStore);
    }

    @Transactional
    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }

    @Transactional
    public StoreDto updateSomeFieldsOfStore(StoreDto storeDto) {
        Store store = storeRepository.findByIdWithAddress(storeDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(STORE_ERROR_MESSAGE, storeDto.getId())));
        Optional.ofNullable(storeDto.getLastUpdate()).ifPresent(store::setLastUpdate);
        return storeMapper.toDto(store);
    }
}
