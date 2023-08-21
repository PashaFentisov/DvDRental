package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.dto.StaffSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Staff;
import com.pashonokk.dvdrental.entity.Store;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.mapper.StaffMapper;
import com.pashonokk.dvdrental.mapper.StaffSavingMapper;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.StaffRepository;
import com.pashonokk.dvdrental.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final CityRepository cityRepository;
    private final StoreRepository storeRepository;
    private final StaffMapper staffMapper;
    private final PageMapper pageMapper;
    private final StaffSavingMapper staffSavingMapper;
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
    public StaffDto addStaff(StaffSavingDto staffSavingDto) {
        Staff staff = staffSavingMapper.toEntity(staffSavingDto);
        staff.setLastUpdate(OffsetDateTime.now());
        Address address = staff.getAddress();
        address.setLastUpdate(OffsetDateTime.now());
        Store store = storeRepository.findById(staffSavingDto.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(STORE_ERROR_MESSAGE, staffSavingDto.getStoreId())));
        staff.addStore(store);
        staff.addAddress(address);
        cityRepository.findByIdWithAddressesAndCountry(staffSavingDto.getAddressSavingDto().getCityId()).ifPresent(city->city.addAddress(address));

        Staff savedStaff = staffRepository.save(staff);
        return staffMapper.toDto(savedStaff);
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
//        Optional.ofNullable(staffDto.getContactInfo().getFirstName()).ifPresent(staff::setFirstName);
//        Optional.ofNullable(staffDto.getContactInfo().getLastName()).ifPresent(staff::setLastName);
//        Optional.ofNullable(staffDto.getContactInfo().getEmail()).ifPresent(staff::setEmail);
//        Optional.ofNullable(staffDto.getContactInfo().getIsActive()).ifPresent(staff.getContactInfo()::setIsActive);
//        Optional.ofNullable(staffDto.getPictureUrl()).ifPresent(staff::setPictureUrl);
        return staffMapper.toDto(staff);
    }
}
