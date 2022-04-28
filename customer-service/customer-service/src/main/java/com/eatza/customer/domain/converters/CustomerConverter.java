package com.eatza.customer.domain.converters;

import com.eatza.customer.domain.model.Customer;
import com.eatza.customer.domain.service.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class CustomerConverter implements Converter<Long, Customer> {

    private final CustomerRepository customerRepository;

    @Override
    public Customer convert(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                String.format("Customer with id = %s not found", customerId)));
    }
}
