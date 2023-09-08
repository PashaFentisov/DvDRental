package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.PhoneDto;
import com.pashonokk.dvdrental.entity.Phone;
import com.pashonokk.dvdrental.mapper.PhoneMapper;
import com.pashonokk.dvdrental.repository.PhoneRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhoneService {
    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;
    private static final String ERROR_MESSAGE = "Phone with id %s doesn't exist";

    @Transactional(readOnly = true)
    public PhoneDto getPhoneById(Long id) {
        Phone phone = phoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        return phoneMapper.toDto(phone);
    }

    @Transactional
    public PhoneDto setNewPhoneAsMain(Long addressId, Long newId) {
        Phone phone = phoneRepository.findById(newId)
                .orElseThrow(()-> new EntityNotFoundException(String.format(ERROR_MESSAGE, newId)));
        phoneRepository.setOldMainPhoneAsNotMain(addressId);
        phone.setMain(true);
        return phoneMapper.toDto(phone);
    }
    @Transactional
    public void deletePhone(Long id) {
        Phone phone = phoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        phone.setDeleted(true);
    }
}
