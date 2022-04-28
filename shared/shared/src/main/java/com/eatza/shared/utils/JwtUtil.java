package com.eatza.shared.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    private final Environment environment;

    public String generateJwt(String username, String authorities) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
                .claim("username", username)
                .claim("authorities", authorities)
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();
    }

    public String authorityToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    }


    public Collection<? extends GrantedAuthority> authorityStringToArray(String authorities) {
        var authoritiesArray = new ArrayList<GrantedAuthority>();
        Arrays.asList(authorities.split(","))
                .forEach(i -> authoritiesArray.add(new SimpleGrantedAuthority(i)));
        return authoritiesArray;
    }
}
