package com.eatza.customer.domain.service;

import com.eatza.customer.domain.converters.RoleConverter;
import com.eatza.customer.domain.dto.CustomerUpdateDto;
import com.eatza.customer.domain.mapper.CustomerMapper;
import com.eatza.customer.domain.model.Customer;
import com.eatza.customer.domain.model.Role;
import com.eatza.shared.config.kafka.producer.sharedProducers.EmailNotificationProducer;
import com.eatza.shared.dto.EmailDto;
import com.eatza.shared.dto.customers.CustomerDto;
import com.eatza.shared.dto.customers.RoleType;
import com.eatza.shared.dto.customers.WalletOperation;
import com.eatza.shared.dto.customers.WalletUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final RoleConverter roleConverter;

    private final EmailNotificationProducer emailNotificationProducer;


    @Override
    public CustomerDto fetchCustomer(Long id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("user with ID = %s not found.", id)));

        return customerMapper.toDto(customer);
    }

    @Override
    public Customer getUserDetailsByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("user with ID = %s not found.", username)));
    }

    @Override
    public boolean existsByEmailOrUsername(String email, String username) {
        return customerRepository.existsByEmailOrUsername(email, username);
    }

    @Override
    public CustomerDto save(Customer customer) {
        return customerMapper.toDto(customerRepository.save(customer));
    }

    @Override
    public CustomerDto updateCustomerDetails(Customer customer, CustomerUpdateDto customerUpdateDto) {

        if (customerRepository.existsByUsername(customerUpdateDto.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Customer with username = %s already exists", customerUpdateDto.getUsername()));

        if (customerRepository.existsByEmail(customerUpdateDto.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Customer with email = %s already exists", customerUpdateDto.getEmail()));

        var customerUpdated = customerMapper.updateCustomer(customerUpdateDto);
        customerUpdated.setRoles(customer.getRoles());
        customerUpdated.setPassword(customer.getPassword());
        customerUpdated.setId(customer.getId());
        customerUpdated.setIsActive(customer.getIsActive());
        customerUpdated.setIsNonLocked(customer.getIsNonLocked());

        return customerMapper.toDto(customerRepository.save(customerUpdated));
    }

    @Override
    public ResponseEntity<String> toggleLock(Customer customer) {
        customerRepository.save(
                customer.setIsNonLocked(!customer.getIsNonLocked())
        );

        var locked = customer.getIsNonLocked() ? "Unlocked Customer %s" : "Locked Customer %s";
        return ResponseEntity.ok(String.format(locked, customer.getUsername()));
    }

    @Override
    public ResponseEntity<String> toggleAdmin(Customer customer) {
        var roles = customer.getRoles();
        var adminAddedFlag = false;

        if (roles.stream().map(Role::getRoleType).collect(Collectors.toList()).contains(RoleType.ROLE_ADMIN)) {
            customer.setRoles(
                    roles.stream().filter(role -> !role.getRoleType().equals(RoleType.ROLE_ADMIN))
                            .collect(Collectors.toSet())
            );
        } else {
            roles.add(roleConverter.convert(RoleType.ROLE_ADMIN));
            adminAddedFlag = true;
        }
        customerRepository.save(customer);

        var locked = adminAddedFlag ? "Added admin role to Customer %s" : "Removed admin priv to Customer %s";
        return ResponseEntity.ok(String.format(locked, customer.getUsername()));
    }

    @Override
    public Optional<Customer> findByToken(String token) {
        return customerRepository.findByToken(token);
    }

    @Override
    public CustomerDto updateWallet(Customer customer, WalletUpdateDto walletUpdateDto) {
        var message = new StringBuilder();
        if (walletUpdateDto.getOperation().equals(WalletOperation.DEDUCT)) {
            var remainingAmount = customer.getWalletAmount() - walletUpdateDto.getAmount();
            customer.setWalletAmount(Math.max(remainingAmount, 0));
            message.append("Amount deducted: ")
                    .append(walletUpdateDto.getAmount());
        } else if (walletUpdateDto.getOperation().equals(WalletOperation.CREDIT)) {
            customer.setWalletAmount(customer.getWalletAmount() + walletUpdateDto.getAmount());
            message.append("Amount Added: ")
                    .append(walletUpdateDto.getAmount());
        }

        message.append("\n Your new balance: ").append(customer.getWalletAmount());

        var emailDto = new EmailDto()
                .setSubject("Your wallet amount is updated.")
                .setUserEmail(customer.getEmail())
                .setMessage(message.toString());
        emailNotificationProducer.send(emailDto);
        return customerMapper.toDto(customerRepository.save(customer));
    }
}
