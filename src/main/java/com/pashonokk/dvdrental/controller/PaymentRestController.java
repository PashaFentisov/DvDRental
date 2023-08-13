package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.PaymentDto;
import com.pashonokk.dvdrental.dto.PaymentSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentRestController {
    private final PaymentService paymentService;
    private final Logger logger = LoggerFactory.getLogger(PaymentRestController.class);


    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
        PaymentDto paymentDto = paymentService.getPaymentById(id);
        return ResponseEntity.ok(paymentDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PaymentDto>> getPayments(@RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "10") int size,
                                                                @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 payments at one time");
        }
        PageResponse<PaymentDto> allPayments = paymentService.getAllPayments(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allPayments);
    }

    @PostMapping
    public ResponseEntity<PaymentDto> addPayment(@RequestBody @Valid PaymentSavingDto paymentSavingDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        PaymentDto savedPayment = paymentService.addPayment(paymentSavingDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPayment.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
