package com.eatza.customer.security.controller;

import com.eatza.customer.security.dto.ConfirmTokenDTO;
import com.eatza.customer.security.dto.LoginDto;
import com.eatza.customer.security.dto.LoginResponseDto;
import com.eatza.customer.security.dto.RegistrationDto;
import com.eatza.customer.security.service.AuthService;
import com.eatza.shared.dto.customers.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/register")
    public CustomerDto login(@RequestBody RegistrationDto registrationDto) {
        return authService.register(registrationDto);
    }

    @GetMapping("/confirm-token/{token}")
    public ConfirmTokenDTO confirmToken(@PathVariable("token") String token) {
        return authService.confirmToken(token);
    }

}
