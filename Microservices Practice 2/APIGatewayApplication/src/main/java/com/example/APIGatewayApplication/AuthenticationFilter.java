package com.example.APIGatewayApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION) || !request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION).startsWith("Bearer ")) {
            logger.error("Missing Authorization header or doesn't contain bearer keyword");
            return onError(exchange);
        }

        String username;
        try {
            String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION).substring(7);
            username = jwtUtil.validateTokenAndGetUsername(token);
        } catch (Exception ex) {
            logger.error("An invalid JWT has been provided");
            return onError(exchange);
        }

        ServerHttpRequest modifiedRequest = request
                .mutate()
                .header("X-Username", username)
                .header("X-Trace-Id", UUID.randomUUID().toString())
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());

    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
