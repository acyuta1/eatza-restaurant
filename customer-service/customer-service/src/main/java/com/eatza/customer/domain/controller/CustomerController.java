package com.eatza.customer.domain.controller;

import com.eatza.customer.domain.dto.CustomerUpdateDto;
import com.eatza.customer.domain.mapper.CustomerMapper;
import com.eatza.customer.domain.model.Customer;
import com.eatza.customer.domain.service.CustomerService;
import com.eatza.shared.dto.customers.CustomerDto;
import com.eatza.shared.dto.customers.WalletUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final CustomerMapper customerMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public CustomerDto getCustomer(@PathVariable("id") Customer customer) {
        return customerMapper.toDto(customer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public CustomerDto updateCustomerDetails(@PathVariable("id") Customer customer,
                                             @RequestBody CustomerUpdateDto customerUpdateDto) {
        return customerService.updateCustomerDetails(customer, customerUpdateDto);
    }

    @PutMapping("/lock-toggle/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> toggleLock(@PathVariable("id") Customer customer) {
        if (customer.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot lock admin");
        }
        return customerService.toggleLock(customer);
    }

    @PutMapping("/admin-toggle/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> toggleAdmin(@PathVariable("id") Customer customer) {
        if (customer.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot alter admin priv of default admin");
        }
        return customerService.toggleAdmin(customer);
    }

    @PutMapping("/{id}/update-wallet")
    @PreAuthorize("hasRole('USER')")
    public CustomerDto updateWallet(@PathVariable("id") Customer customer, @RequestBody @Valid WalletUpdateDto walletUpdateDto) {
        return customerService.updateWallet(customer, walletUpdateDto);
    }
}
