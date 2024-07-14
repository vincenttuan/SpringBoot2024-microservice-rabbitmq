package com.example.demo;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
        		
                .route("feign-customer-service-9092", r -> r.path("/customers/**")
                		// f: GatewayFilterSpec, c: CircuitBreakerConfig
                		.filters(f -> f.circuitBreaker(c -> c.setName("customerCircuitBreaker")
                				.setFallbackUri("forward:/fallback"))
                				.retry(config -> config.setRetries(2).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)))
                        .uri("lb://feign-customer-service-9092"))
                
                .route("feign-product-service-9091", r -> r.path("/products/**")
                        .uri("lb://feign-product-service-9091"))
                
                .route("feign-order-service-9093", r -> r.path("/orders/**")
                        .uri("lb://feign-order-service-9093"))
                
                .build();
    }
}
