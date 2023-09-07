package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.dto.UserStaffSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Phone;
import com.pashonokk.dvdrental.entity.Staff;
import com.pashonokk.dvdrental.entity.Store;
import com.pashonokk.dvdrental.mapper.AddressSavingMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.mapper.StaffMapper;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.StaffRepository;
import com.pashonokk.dvdrental.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final CityRepository cityRepository;
    private final StoreRepository storeRepository;
    private final StaffMapper staffMapper;
    private final PageMapper pageMapper;
    private final AddressSavingMapper addressSavingMapper;
    private static final String STAFF_ERROR_MESSAGE = "Staff with id %s doesn't exist";
    private static final String STORE_ERROR_MESSAGE = "Store with id %s doesn't exist";

    @Transactional(readOnly = true)
    public StaffDto getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(STAFF_ERROR_MESSAGE, id)));
        return staffMapper.toDto(staff);
    }

    @Transactional(readOnly = true)
    public PageResponse<StaffDto> getAllStaff(Pageable pageable) {
        return pageMapper.toPageResponse(staffRepository.findAll(pageable).map(staffMapper::toDto));
    }

    @Transactional
    public Staff constructStaff(UserStaffSavingDto userDto) {
        Phone phone = Phone.builder()
                .number(userDto.getAddress().getNumber())
                .isMain(true)
                .isDeleted(false)
                .build();
        Staff staff = new Staff(userDto.getPictureUrl(), OffsetDateTime.now(), false);
        Address address = addressSavingMapper.toEntity(userDto.getAddress());
        address.setLastUpdate(OffsetDateTime.now());
        address.setIsDeleted(false);
        Store store = storeRepository.findById(userDto.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(STORE_ERROR_MESSAGE, userDto.getStoreId())));
        staff.addStore(store);
        staff.addAddress(address);
        phone.addAddress(address);
        cityRepository.findByIdWithAddressesAndCountry(userDto.getAddress().getCityId()).ifPresent(city->city.addAddress(address));
        return staff;
    }

    @Transactional
    public void deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(STAFF_ERROR_MESSAGE, id)));
        staff.setIsDeleted(true);
        staff.getAddress().setIsDeleted(true);

    }

    @Transactional
    public StaffDto updateSomeFieldsOfStaff(StaffDto staffDto) {
        Staff staff = staffRepository.findById(staffDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(STAFF_ERROR_MESSAGE, staffDto.getId())));
        Optional.ofNullable(staffDto.getPictureUrl()).ifPresent(staff::setPictureUrl);
        return staffMapper.toDto(staff);
    }
}
