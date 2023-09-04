package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.RentalDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Rental;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.mapper.RentalMapper;
import com.pashonokk.dvdrental.repository.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final PageMapper pageMapper;
    private static final String RENTAL_ERROR_MESSAGE = "Rental with id %s doesn't exist";

    @Transactional(readOnly = true)
    public RentalDto getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(RENTAL_ERROR_MESSAGE, id)));
        return rentalMapper.toDto(rental);
    }

    @Transactional(readOnly = true)
    public PageResponse<RentalDto> getAllRentals(Pageable pageable) {
        return pageMapper.toPageResponse(rentalRepository.findAll(pageable).map(rentalMapper::toDto));
    }

    @Transactional
    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(String.format(RENTAL_ERROR_MESSAGE, id)));
        rental.setIsDeleted(true);
    }
}
