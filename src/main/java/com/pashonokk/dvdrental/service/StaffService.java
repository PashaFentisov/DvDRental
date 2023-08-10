package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.dto.StaffSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Staff;
import com.pashonokk.dvdrental.entity.Store;
import com.pashonokk.dvdrental.exception.StoreWithoutAddressException;
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

import java.util.Optional;

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
        Address address = staff.getAddress();
        if(address==null){
            throw new StoreWithoutAddressException("Staff cannot exist without an address");
        }
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
        staffRepository.deleteById(id);
    }

    @Transactional
    public StaffDto updateSomeFieldsOfStaff(StaffDto staffDto) {
        Staff staff = staffRepository.findById(staffDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(STAFF_ERROR_MESSAGE, staffDto.getId())));
        Optional.ofNullable(staffDto.getFirstName()).ifPresent(staff::setFirstName);
        Optional.ofNullable(staffDto.getLastName()).ifPresent(staff::setLastName);
        Optional.ofNullable(staffDto.getEmail()).ifPresent(staff::setEmail);
        Optional.ofNullable(staffDto.getActive()).ifPresent(staff::setActive);
        Optional.ofNullable(staffDto.getUsername()).ifPresent(staff::setUsername);
        Optional.ofNullable(staffDto.getPassword()).ifPresent(staff::setPassword);
        Optional.ofNullable(staffDto.getPictureUrl()).ifPresent(staff::setPictureUrl);
        Optional.ofNullable(staffDto.getLastUpdate()).ifPresent(staff::setLastUpdate);
        return staffMapper.toDto(staff);
    }
}
