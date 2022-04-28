package com.eatza.shared.feign.customers;

import com.eatza.shared.dto.customers.CustomerDto;
import com.eatza.shared.dto.customers.WalletUpdateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "customer-management-service/customers")
public interface CustomerClient {

    @GetMapping("/{id}")
    CustomerDto getCustomer(@PathVariable("id") Long id);

    @PutMapping("/{id}/update-wallet")
    CustomerDto updateWallet(@PathVariable("id") Long id, @RequestBody @Valid WalletUpdateDto walletUpdateDto);
}
