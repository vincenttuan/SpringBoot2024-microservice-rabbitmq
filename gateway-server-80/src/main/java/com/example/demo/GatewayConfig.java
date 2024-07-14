package com.example.demo;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
        		
                .route("feign-customer-service-9092", r -> r.path("/customers/**")
                        .uri("lb://feign-customer-service-9092"))
                
                .route("feign-product-service-9091", r -> r.path("/products/**")
                        .uri("lb://feign-product-service-9091"))
                
                .route("feign-order-service-9093", r -> r.path("/orders/**")
                        .uri("lb://feign-order-service-9093"))
                
                .build();
    }
}
