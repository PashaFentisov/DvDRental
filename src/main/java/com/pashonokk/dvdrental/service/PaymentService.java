package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.PaymentDto;
import com.pashonokk.dvdrental.dto.PaymentSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.entity.Payment;
import com.pashonokk.dvdrental.entity.Rental;
import com.pashonokk.dvdrental.entity.Staff;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.mapper.PaymentMapper;
import com.pashonokk.dvdrental.mapper.PaymentSavingMapper;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import com.pashonokk.dvdrental.repository.PaymentRepository;
import com.pashonokk.dvdrental.repository.RentalRepository;
import com.pashonokk.dvdrental.repository.StaffRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentSavingMapper paymentSavingMapper;
    private final PageMapper pageMapper;
    private static final String RENTAL_ERROR_MESSAGE = "Rental with id %s doesn't exist";
    private static final String STAFF_ERROR_MESSAGE = "Staff with id %s doesn't exist";
    private static final String CUSTOMER_ERROR_MESSAGE = "Customer with id %s doesn't exist";
    private static final String PAYMENT_ERROR_MESSAGE = "Payment with id %s doesn't exist";


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
    public PaymentDto addPayment(PaymentSavingDto paymentSavingDto) {
        Payment payment = paymentSavingMapper.toEntity(paymentSavingDto);

        Staff staff = staffRepository.findById(paymentSavingDto.getStaffId())
                .orElseThrow(()->new EntityNotFoundException(String.format(STAFF_ERROR_MESSAGE, paymentSavingDto.getStaffId())));

        Customer customer = customerRepository.findCustomerById(paymentSavingDto.getCustomerId())
                .orElseThrow(()->new EntityNotFoundException(String.format(CUSTOMER_ERROR_MESSAGE,paymentSavingDto.getCustomerId())));

        Rental rental = rentalRepository.findById(paymentSavingDto.getRentalId())
                .orElseThrow(()->new EntityNotFoundException(String.format(RENTAL_ERROR_MESSAGE,paymentSavingDto.getRentalId())));

        payment.addCustomer(customer);
        payment.addStaff(staff);
        payment.addRental(rental);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(String.format(PAYMENT_ERROR_MESSAGE, id)));
        paymentRepository.delete(payment);
    }

}
