package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.RentalDto;
import com.pashonokk.dvdrental.dto.RentalSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.entity.Inventory;
import com.pashonokk.dvdrental.entity.Rental;
import com.pashonokk.dvdrental.entity.Staff;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.mapper.RentalMapper;
import com.pashonokk.dvdrental.mapper.RentalSavingMapper;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import com.pashonokk.dvdrental.repository.InventoryRepository;
import com.pashonokk.dvdrental.repository.RentalRepository;
import com.pashonokk.dvdrental.repository.StaffRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final InventoryRepository inventoryRepository;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final RentalMapper rentalMapper;
    private final RentalSavingMapper rentalSavingMapper;
    private final PageMapper pageMapper;
    private static final String RENTAL_ERROR_MESSAGE = "Rental with id %s doesn't exist";
    private static final String STAFF_ERROR_MESSAGE = "Staff with id %s doesn't exist";
    private static final String CUSTOMER_ERROR_MESSAGE = "Customer with id %s doesn't exist";
    private static final String INVENTORY_ERROR_MESSAGE = "Inventory with id %s doesn't exist";


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
    public RentalDto addRental(RentalSavingDto rentalSavingDto) {
        Rental rental = rentalSavingMapper.toEntity(rentalSavingDto);

        Staff staff = staffRepository.findById(rentalSavingDto.getStaffId())
                .orElseThrow(()->new EntityNotFoundException(String.format(STAFF_ERROR_MESSAGE, rentalSavingDto.getStaffId())));

        Customer customer = customerRepository.findCustomerById(rentalSavingDto.getCustomerId())
                .orElseThrow(()->new EntityNotFoundException(String.format(CUSTOMER_ERROR_MESSAGE,rentalSavingDto.getCustomerId())));

        Inventory inventory = inventoryRepository.findById(rentalSavingDto.getInventoryId())
                .orElseThrow(()->new EntityNotFoundException(String.format(INVENTORY_ERROR_MESSAGE,rentalSavingDto.getInventoryId())));
        rental.addCustomer(customer);
        rental.addStaff(staff);
        rental.addInventory(inventory);

        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toDto(savedRental);
    }

    @Transactional
    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(String.format(RENTAL_ERROR_MESSAGE, id)));
        rental.removePayment(rental.getPayment());
        rentalRepository.delete(rental);
    }

}
