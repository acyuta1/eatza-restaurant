package com.eatza.ApiGateway.filters;

import io.jsonwebtoken.Jwts;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private Environment environment;

    public static class Config {

    }

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            var req = exchange.getRequest();
            System.out.println(req);

            if (!req.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header found!", HttpStatus.UNAUTHORIZED);
            }

            var authorizationValue = req.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            var jwt = authorizationValue.replace("Bearer ", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "Invalid JWT!", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        var response = exchange.getResponse();

        response.setStatusCode(status);

        return response.setComplete();
    }

    private Boolean isJwtValid(String jwt) {

        String subject = null;
        try {
            subject = Jwts
                    .parser()
                    .setSigningKey(environment.getProperty("token.secret"))
                    .parseClaimsJws(jwt)
                    .getBody().getSubject();

        } catch (Exception e) {
            throw e;
        }
        return subject != null && !subject.isBlank();

    }
}
