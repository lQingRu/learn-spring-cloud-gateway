package com.learning.springgateway.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory {
    private static final String HEADER_NAME = "X-Forwarded-Prefix";

    @Override
    public GatewayFilter apply(Object config) {
        System.out.println("apply");
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            System.out.println(path);

            if (!StringUtils.endsWithIgnoreCase(path, "/v3/api-docs")) {
                return chain.filter(exchange);
            }

            String basePath = path.substring(0, path.lastIndexOf("/v3/api-docs"));


            ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).build();
            System.out.println(newRequest.getPath());
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);
        };
    }
}
