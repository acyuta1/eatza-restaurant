package com.eatza.customer.security.service;

import com.eatza.customer.domain.converters.RoleConverter;
import com.eatza.customer.domain.mapper.CustomerMapper;
import com.eatza.customer.domain.service.CustomerService;
import com.eatza.customer.domain.service.RedisRepository;
import com.eatza.customer.security.config.UserDetailsImpl;
import com.eatza.customer.security.dto.ConfirmTokenDTO;
import com.eatza.customer.security.dto.LoginDto;
import com.eatza.customer.security.dto.LoginResponseDto;
import com.eatza.customer.security.dto.RegistrationDto;
import com.eatza.shared.config.kafka.producer.sharedProducers.EmailNotificationProducer;
import com.eatza.shared.config.redis.RedisJwtStore;
import com.eatza.shared.dto.EmailDto;
import com.eatza.shared.dto.customers.CustomerDto;
import com.eatza.shared.dto.customers.RoleType;
import com.eatza.shared.utils.JwtUtil;
import com.eatza.shared.utils.MailSenderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final CustomerService customerService;

    private final JwtUtil jwtUtils;

    private final RoleConverter roleConverter;

    private final CustomerMapper customerMapper;

    private final EmailNotificationProducer emailNotificationProducer;

    private final RedisRepository redisJwtRepository;


    @Override
    public LoginResponseDto login(LoginDto loginDto) {

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        var userDetails = (UserDetailsImpl) auth.getPrincipal();

        var jwt = jwtUtils.generateJwt(userDetails.getUsername(), jwtUtils.authorityToString(userDetails.getAuthorities()));

        redisJwtRepository.deleteAll();

        redisJwtRepository.save(new RedisJwtStore(null, jwt, userDetails.getUsername()));

        return new LoginResponseDto()
                .setJwt(jwt)
                .setUsername(userDetails.getUsername());
    }

    @Override
    @Transactional
    public CustomerDto register(RegistrationDto registrationDto) {
        var isCustomerPresent =
                customerService.existsByEmailOrUsername(registrationDto.getEmail(), registrationDto.getUsername());

        if (isCustomerPresent)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customer.already.present");

        var token = UUID.randomUUID().toString();

        var customer = customerMapper.fromRegistrationDto(registrationDto);

        customer
                .setPassword(passwordEncoder.encode(registrationDto.getPassword()))
                .setRoles(Set.of(roleConverter.convert(RoleType.ROLE_USER)))
                .setToken(token);

        var emailDto = new EmailDto();
        emailDto.setSubject("Confirm your E-mail")
                .setMessage(String.format("Dear %s,\n Please click on this link to confirm your email %s/confirm-token/%s",
                        customer.getFirstName(),
                        "http://localhost:8082/auth",
                        token))
                .setUserEmail(customer.getEmail());
        emailNotificationProducer.send(emailDto);

        return customerService.save(customer);
    }

    @Override
    public ConfirmTokenDTO confirmToken(String token) {
        var customerOpt = customerService.findByToken(token);

        if (!customerOpt.isPresent()) {
            return new ConfirmTokenDTO().setAuthenticated(false);
        }

        var customer = customerOpt.get();

        var customerTokenDto = new ConfirmTokenDTO()
                .setAuthenticated(true)
                .setUsername(customer.getUsername());

        customerService.save(customer.setToken(null).setIsActive(true));
        return customerTokenDto;
    }
}
