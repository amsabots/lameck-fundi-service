package com.amsabots.jenzi.fundi_service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Component

public class ResponseFilter implements org.springframework.web.server.WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders httpHeaders = exchange.getResponse().getHeaders();
        httpHeaders.add("Access-Control-Allow-Origin", "*");
        httpHeaders.add("Access-Control-Allow-Headers", "*");
        httpHeaders.add("Access-Control-Request-Method", "*");
        httpHeaders.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        return chain.filter(exchange);
    }
}
