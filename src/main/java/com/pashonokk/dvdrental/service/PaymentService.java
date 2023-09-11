package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.*;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.*;
import com.pashonokk.dvdrental.exception.GenericDisplayableException;
import com.pashonokk.dvdrental.mapper.*;
import com.pashonokk.dvdrental.repository.*;
import com.pashonokk.dvdrental.util.PaymentProperties;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final InventoryRepository inventoryRepository;
    private final HolidayRepository holidayRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PaymentMapper paymentMapper;
    private final PageMapper pageMapper;
    private static final String INVENTORY_ERROR_MESSAGE = "Inventory with film id %s and store id %s doesn't exist";
    private static final String CUSTOMER_ERROR_MESSAGE = "Customer with id %s doesn't exist";
    private static final String PAYMENT_ERROR_MESSAGE = "Payment with id %s doesn't exist";
    private final PaymentProperties paymentProperties;
    private final CustomerMapper customerMapper;
    private final FilmMapper filmMapper;
    private final StoreMapper storeMapper;


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
    public List<PaymentDto> createPayments(MultiplePaymentSavingDto multiplePaymentSavingDto, String email) {
        List<PaymentDto> createdPayments = new ArrayList<>();
        PaymentSavingDto paymentSavingDto;
        int paymentCount = 0;
        boolean isFilmFree = false;
        for (RentalRequestDto rentalRequestDto : multiplePaymentSavingDto.getRentals()) {
            paymentCount++;
            paymentSavingDto = PaymentSavingDto.builder()
                    .filmId(rentalRequestDto.getFilmId())
                    .rentalDays(rentalRequestDto.getRentalDays())
                    .customerId(multiplePaymentSavingDto.getCustomerId())
                    .build();
            if (paymentCount == 4) {
                isFilmFree = true;
            }
            PaymentDto payment = createPayment(paymentSavingDto, email, isFilmFree);
            createdPayments.add(payment);
        }
        return createdPayments;
    }


    @Transactional
    public PaymentDto createPayment(PaymentSavingDto paymentSavingDto, String email, boolean isFilmFree) {
        List<Payment> openPaymentsWithSameFilm = paymentRepository.findOpenPaymentsWithSameFilm(paymentSavingDto.getCustomerId(), paymentSavingDto.getFilmId());
        if (!openPaymentsWithSameFilm.isEmpty()) {
            throw new GenericDisplayableException(HttpStatus.BAD_REQUEST, "You can`t take two same films, return the old one");
        }
        Staff staff = userRepository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new).getStaff();
        Customer customer = customerRepository.findCustomerById(paymentSavingDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(CUSTOMER_ERROR_MESSAGE, paymentSavingDto.getCustomerId())));

        Inventory inventory = getInventory(paymentSavingDto, staff);

        Rental rental = buildRental(paymentSavingDto, staff, inventory, customer);
        BigDecimal amount = calculatePaymentAmount(paymentSavingDto, isFilmFree);

        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now());
        LocalDate holidayDate = LocalDate.now().plusDays(paymentSavingDto.getRentalDays());
        Payment payment = new Payment(amount, OffsetDateTime.of(holidayDate, LocalTime.MIDNIGHT, offset), false);

        payment.addCustomer(customer);
        payment.addStaff(staff);
        payment.addRental(rental);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    private BigDecimal calculatePaymentAmount(PaymentSavingDto paymentSavingDto, boolean isFilmFree) {
        if (isFilmFree) {
            return BigDecimal.ZERO;
        }
        Long customersOpenPayments = paymentRepository.countOpenPaymentsByCustomerId(paymentSavingDto.getCustomerId());
        BigDecimal amountWithoutDiscount = paymentProperties.getPrice().multiply(BigDecimal.valueOf(paymentSavingDto.getRentalDays()));
        if (customersOpenPayments >= 10 && customersOpenPayments < 20) {
            amountWithoutDiscount = amountWithoutDiscount.subtract(amountWithoutDiscount.multiply(BigDecimal.valueOf(0.05)));
        } else if (customersOpenPayments >= 20 && customersOpenPayments < 30) {
            amountWithoutDiscount = amountWithoutDiscount.subtract(amountWithoutDiscount.multiply(BigDecimal.valueOf(0.10)));
        } else if (customersOpenPayments >= 30) {
            amountWithoutDiscount = amountWithoutDiscount.subtract(amountWithoutDiscount.multiply(BigDecimal.valueOf(0.15)));
        }
        return amountWithoutDiscount;
    }

    private Inventory getInventory(PaymentSavingDto paymentSavingDto, Staff staff) {
        Inventory inventory;
        List<Inventory> inventories = inventoryRepository
                .findByFilmAndStore(paymentSavingDto.getFilmId(), staff.getStore().getId());
        if (inventories.isEmpty()) {
            throw new EntityNotFoundException(String.format(INVENTORY_ERROR_MESSAGE, paymentSavingDto.getFilmId(),
                    staff.getStore().getId()));
        } else {
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
                .isClosed(false)
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
        rental.setIsClosed(true);

        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now());
        OffsetDateTime now = OffsetDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, offset);

        BigDecimal totalAmount = payment.getAmount();
        if (payment.getPaymentDate().isBefore(now)) {
            totalAmount = countTotalAmountWithFine(payment, now);
        }
        return buildClosedPaymentResponse(payment, totalAmount, inventory, rental, now);
    }

    private ClosedPaymentResponse buildClosedPaymentResponse(Payment payment, BigDecimal totalAmount,
                                                             Inventory inventory, Rental rental,
                                                             OffsetDateTime now) {
        long extraDays = 0;
        if (payment.getPaymentDate().isBefore(now)) {
            extraDays = countExtraDays(payment, now);
        }
        return ClosedPaymentResponse.builder()
                .customer(customerMapper.toDto(payment.getCustomer()))
                .film(filmMapper.toDto(inventory.getFilm()))
                .extraDays(extraDays)
                .fineAmount(totalAmount.subtract(payment.getAmount()))
                .store(storeMapper.toDto(inventory.getStore()))
                .totalAmount(totalAmount)
                .rentalDate(rental.getRentalDate())
                .returnDate(OffsetDateTime.now())
                .build();
    }

    private BigDecimal countTotalAmountWithFine(Payment payment, OffsetDateTime now) {
        long extraDays = countExtraDays(payment, now);
        if (extraDays == 0) {
            return payment.getAmount();
        }
        return payment.getAmount().add(paymentProperties.getFine().multiply(BigDecimal.valueOf(extraDays)));
    }

    private long countExtraDays(Payment payment, OffsetDateTime now) {
        long extraDays = Duration.between(payment.getPaymentDate().toLocalDateTime(),
                LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)).toDays();
        int holidays = holidayRepository.countHolidayBetweenDates(payment.getPaymentDate(), now);
        return (extraDays < holidays) ? 0 : extraDays - holidays;
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PAYMENT_ERROR_MESSAGE, id)));
        payment.setIsClosed(true);
    }

}
