package com.eatza.customer.security.service;

import com.eatza.customer.security.dto.ConfirmTokenDTO;
import com.eatza.customer.security.dto.LoginDto;
import com.eatza.customer.security.dto.LoginResponseDto;
import com.eatza.customer.security.dto.RegistrationDto;
import com.eatza.shared.dto.customers.CustomerDto;

public interface AuthService {

    LoginResponseDto login(LoginDto loginDto);

    CustomerDto register(RegistrationDto registrationDto);

    ConfirmTokenDTO confirmToken(String token);
}
