package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.ClosedPaymentResponse;
import com.pashonokk.dvdrental.dto.PaymentClosingDto;
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
import com.pashonokk.dvdrental.util.PaymentProperties;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Objects;

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
    private final PaymentProperties paymentProperties;


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
        Customer customer = customerRepository.findCustomerById(paymentSavingDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(CUSTOMER_ERROR_MESSAGE, paymentSavingDto.getCustomerId())));

        Inventory inventory = getInventory(paymentSavingDto, staff);

        Rental rental = buildRental(paymentSavingDto, staff, inventory, customer);
        BigDecimal amount = paymentProperties.getPrice().multiply(BigDecimal.valueOf(paymentSavingDto.getRentalDays()));
        Payment payment = new Payment(amount, OffsetDateTime.now().plusDays(paymentSavingDto.getRentalDays()), false);

        payment.addCustomer(customer);
        payment.addStaff(staff);
        payment.addRental(rental);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    private Inventory getInventory(PaymentSavingDto paymentSavingDto, Staff staff) {
        Inventory inventory;
        List<Inventory> inventories = inventoryRepository
                .findByFilmAndStore(paymentSavingDto.getFilmId(), staff.getStore().getId());
        if (inventories.isEmpty()) {
            throw new EntityNotFoundException(String.format(INVENTORY_ERROR_MESSAGE, paymentSavingDto.getFilmId(),
                                                                                     staff.getStore().getId()));
        }else{
            inventory = inventories.get(0);
            inventory.setIsAvailable(false);
        }
        return inventory;
    }

    private static Rental buildRental(PaymentSavingDto paymentSavingDto, Staff staff, Inventory inventory, Customer customer) {
        return Rental.builder()
                .rentalDate(OffsetDateTime.now())
                .returnDate(OffsetDateTime.now().plusDays(paymentSavingDto.getRentalDays()))
                .lastUpdate(OffsetDateTime.now())
                .isDeleted(false)
                .inventory(inventory)
                .customer(customer)
                .staff(staff)
                .build();
    }

    @Transactional
    public ClosedPaymentResponse closePayment(PaymentClosingDto paymentClosingDto) {
        Payment payment = paymentRepository.findOpenPayments(paymentClosingDto.getCustomerId())
                .stream()
                .filter(p -> Objects.equals(p.getRental().getInventory().getFilm().getId(), paymentClosingDto.getFilmId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Payment doesn`t exists, something is wrong"));

        Rental rental = payment.getRental();
        Inventory inventory = rental.getInventory();

        inventory.setIsAvailable(true);
        payment.setIsClosed(true);
        rental.setIsDeleted(true);

        BigDecimal totalAmount = payment.getAmount();
        if(payment.getPaymentDate().isBefore(OffsetDateTime.now())){
            totalAmount = countTotalAmountWithFine(payment);
        }

        return buildClosedPaymentResponse(payment, totalAmount, inventory, rental);
    }

    private ClosedPaymentResponse buildClosedPaymentResponse(Payment payment, BigDecimal totalAmount, Inventory inventory, Rental rental) {
        long extraDays = 0;
        if(payment.getPaymentDate().isBefore(OffsetDateTime.now())){
            extraDays = Duration.between(payment.getPaymentDate().toLocalDateTime(), LocalDateTime.now()).toDays();
        }
        return ClosedPaymentResponse.builder()
                .customerId(payment.getCustomer().getId())
                .filmId(inventory.getFilm().getId())
                .extraDays(extraDays)
                .fineAmount(totalAmount.subtract(payment.getAmount()))
                .storeId(inventory.getStore().getId())
                .totalAmount(totalAmount)
                .rentalDate(rental.getRentalDate())
                .returnDate(OffsetDateTime.now())
                .build();
    }

    private BigDecimal countTotalAmountWithFine(Payment payment) {
        long extraDays = Duration.between(payment.getPaymentDate().toLocalDateTime(), LocalDateTime.now()).toDays();
        return payment.getAmount().add(paymentProperties.getFine().multiply(BigDecimal.valueOf(extraDays)));
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PAYMENT_ERROR_MESSAGE, id)));
        payment.setIsClosed(true);
    }

}
