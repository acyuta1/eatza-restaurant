package com.eatza.customer.domain.service;

import com.eatza.customer.domain.dto.CustomerUpdateDto;
import com.eatza.customer.domain.model.Customer;
import com.eatza.shared.dto.customers.CustomerDto;
import com.eatza.shared.dto.customers.WalletUpdateDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CustomerService {
    CustomerDto fetchCustomer(Long id);

    Customer getUserDetailsByUsername(String username);

    boolean existsByEmailOrUsername(String email, String username);

    CustomerDto save(Customer customer);

    CustomerDto updateCustomerDetails(Customer customer, CustomerUpdateDto customerUpdateDto);

    ResponseEntity<String> toggleLock(Customer customer);

    ResponseEntity<String> toggleAdmin(Customer customer);

    Optional<Customer> findByToken(String token);

    CustomerDto updateWallet(Customer customer, WalletUpdateDto topupWalletDto);

//    ReviewRequestDto postReview(ReviewRequestDto reviewRequestDto);
}
