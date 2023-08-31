package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.PaymentDto;
import com.pashonokk.dvdrental.dto.PaymentSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.*;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.mapper.PaymentMapper;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import com.pashonokk.dvdrental.repository.InventoryRepository;
import com.pashonokk.dvdrental.repository.PaymentRepository;
import com.pashonokk.dvdrental.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PaymentMapper paymentMapper;
    private final PageMapper pageMapper;
    private static final String INVENTORY_ERROR_MESSAGE = "Inventory with film id %s and store id %s doesn't exist";
    private static final String CUSTOMER_ERROR_MESSAGE = "Customer with id %s doesn't exist";
    private static final String PAYMENT_ERROR_MESSAGE = "Payment with id %s doesn't exist";
    @Value("${dvd.oneday.price}")
    private BigDecimal price;


    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PAYMENT_ERROR_MESSAGE, id)));
        return paymentMapper.toDto(payment);
    }

    @Transactional(readOnly = true)
    public PageResponse<PaymentDto> getAllPayments(Pageable pageable) {
        return pageMapper.toPageResponse(paymentRepository.findAll(pageable).map(paymentMapper::toDto));
    }

    @Transactional
    public PaymentDto createPayment(PaymentSavingDto paymentSavingDto, String email) {
        Staff staff = userRepository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new).getStaff();
        Inventory inventory;
        Customer customer = customerRepository.findCustomerById(paymentSavingDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(CUSTOMER_ERROR_MESSAGE, paymentSavingDto.getCustomerId())));

        List<Inventory> inventories = inventoryRepository
                .findByFilmAndStore(paymentSavingDto.getFilmId(), paymentSavingDto.getStoreId());
        if (inventories.isEmpty()) {
            throw new EntityNotFoundException(String.format(INVENTORY_ERROR_MESSAGE, paymentSavingDto.getFilmId(),
                                                                                     paymentSavingDto.getStoreId()));
        }else{
            inventory = inventories.get(0);
            inventory.setIsAvailable(false);
        }

        Rental rental = Rental.builder()
                .rentalDate(OffsetDateTime.now())
                .returnDate(OffsetDateTime.now().plusDays(paymentSavingDto.getRentalDays()))
                .lastUpdate(OffsetDateTime.now())
                .isDeleted(false)
                .inventory(inventory)
                .customer(customer)
                .staff(staff)
                .build();
        BigDecimal amount = price.multiply(BigDecimal.valueOf(paymentSavingDto.getRentalDays()));
        Payment payment = new Payment(amount, OffsetDateTime.now().plusDays(paymentSavingDto.getRentalDays()), false);

        payment.addCustomer(customer);
        payment.addStaff(staff);
        payment.addRental(rental);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PAYMENT_ERROR_MESSAGE, id)));
        payment.setIsClosed(true);
    }

}
