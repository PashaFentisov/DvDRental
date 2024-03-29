package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.*;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.*;
import com.pashonokk.dvdrental.exception.GenericDisplayableException;
import com.pashonokk.dvdrental.exception.LowBalanceException;
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
        BigDecimal discount = calculateDiscount(paymentSavingDto);
        BigDecimal amount = calculatePaymentAmount(paymentSavingDto, isFilmFree, discount);

        payForFilm(customer, amount);

        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now());
        LocalDate holidayDate = LocalDate.now().plusDays(paymentSavingDto.getRentalDays());
        Payment payment = new Payment(amount, OffsetDateTime.of(holidayDate, LocalTime.MIDNIGHT, offset), false, discount);

        payment.addCustomer(customer);
        payment.addStaff(staff);
        payment.addRental(rental);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }

    private static void payForFilm(Customer customer, BigDecimal amount) {
        if (customer.getBalance().compareTo(amount) < 0) {
            throw new LowBalanceException(
                    String.format("Your don`t have enough money on your balance, your balance is %s", customer.getBalance()));
        } else {
            customer.setBalance(customer.getBalance().subtract(amount));
        }
    }

    private BigDecimal calculatePaymentAmount(PaymentSavingDto paymentSavingDto, boolean isFilmFree, BigDecimal discount) {
        if (isFilmFree) {
            return BigDecimal.ZERO;
        }
        BigDecimal amount = paymentProperties.getPrice().multiply(BigDecimal.valueOf(paymentSavingDto.getRentalDays()));
        if (!Objects.equals(discount, BigDecimal.ZERO)) {
            amount = amount.subtract(amount.multiply(discount));
        }
        return amount;
    }

    private BigDecimal calculateDiscount(PaymentSavingDto paymentSavingDto) {
        Long customersOpenPayments = paymentRepository.countOpenPaymentsByCustomerId(paymentSavingDto.getCustomerId());
        BigDecimal discount = BigDecimal.ZERO;
        if (customersOpenPayments >= 10 && customersOpenPayments < 20) {
            discount = BigDecimal.valueOf(0.05);
        } else if (customersOpenPayments >= 20 && customersOpenPayments < 30) {
            discount = BigDecimal.valueOf(0.10);
        } else if (customersOpenPayments >= 30) {
            discount = BigDecimal.valueOf(0.15);
        }
        return discount;
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
        Customer customer = payment.getCustomer();
        Rental rental = payment.getRental();
        Inventory inventory = rental.getInventory();

        inventory.setIsAvailable(true);
        payment.setIsClosed(true);
        rental.setIsClosed(true);

        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now());
        OffsetDateTime now = OffsetDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, offset);

        BigDecimal totalAmount = payment.getAmount();
        if (payment.getPaymentDate().isBefore(now)) {
            BigDecimal fine = chargeFineFromBalance(payment, now, customer);
            totalAmount = totalAmount.add(fine);
        } else if (payment.getPaymentDate().isAfter(now)) {
            BigDecimal change = returnUnusedMoneyToBalance(payment, customer);
            totalAmount = totalAmount.subtract(change);
        }
        return buildClosedPaymentResponse(payment, totalAmount, inventory, rental, now);
    }

    private ClosedPaymentResponse buildClosedPaymentResponse(Payment payment, BigDecimal totalAmount,
                                                             Inventory inventory, Rental rental,
                                                             OffsetDateTime now) {
        long extraDays = 0;
        BigDecimal fine;
        if (totalAmount.compareTo(payment.getAmount()) <= 0) {
            fine = BigDecimal.ZERO;
        } else {
            fine = totalAmount.subtract(payment.getAmount());
        }
        if (payment.getPaymentDate().isBefore(now)) {
            extraDays = countExtraDays(payment, now);
        }
        return ClosedPaymentResponse.builder()
                .customer(customerMapper.toDto(payment.getCustomer()))
                .film(filmMapper.toDto(inventory.getFilm()))
                .extraDays(extraDays)
                .fineAmount(fine)
                .store(storeMapper.toDto(inventory.getStore()))
                .totalAmount(totalAmount)
                .rentalDate(rental.getRentalDate())
                .returnDate(OffsetDateTime.now())
                .discount(payment.getDiscount())
                .build();
    }

    @Transactional
    public BigDecimal chargeFineFromBalance(Payment payment, OffsetDateTime now, Customer customer) {
        long extraDays = countExtraDays(payment, now);
        BigDecimal fine = paymentProperties.getFine().multiply(BigDecimal.valueOf(extraDays));
        if (customer.getBalance().compareTo(fine) < 0) {
            throw new LowBalanceException(
                    String.format("Your don`t have enough money on your balance, your balance is %s", customer.getBalance()));
        } else {
            customer.setBalance(customer.getBalance().subtract(fine));
        }
        payment.setAmount(payment.getAmount().add(fine));
        return fine;
    }

    @Transactional
    public BigDecimal returnUnusedMoneyToBalance(Payment payment, Customer customer) {
        long unusedDays = countUnusedDays(payment);
        BigDecimal change = paymentProperties.getPrice().multiply(BigDecimal.valueOf(unusedDays));
        if (!Objects.equals(payment.getDiscount(), BigDecimal.ZERO)) {
            change = change.subtract(change.multiply(payment.getDiscount()));
        }
        payment.setAmount(payment.getAmount().subtract(change));
        customer.setBalance(customer.getBalance().add(change));
        return change;
    }

    private long countExtraDays(Payment payment, OffsetDateTime now) {
        long extraDays = Duration.between(payment.getPaymentDate().toLocalDateTime(),
                LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)).toDays();
        int holidays = holidayRepository.countHolidayBetweenDates(payment.getPaymentDate(), now);
        return (extraDays < holidays) ? 0 : extraDays - holidays;
    }

    private long countUnusedDays(Payment payment) {
        return Duration.between(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
                payment.getPaymentDate().toLocalDateTime()).toDays();
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PAYMENT_ERROR_MESSAGE, id)));
        payment.setIsClosed(true);
    }

}
