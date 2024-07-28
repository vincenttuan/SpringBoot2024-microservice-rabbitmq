package com.example.demo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
	
	/*
	 * @LoadBalanced註解：這個版本的方法使用了@LoadBalanced註解，
	 * 這意味著這個RestTemplate實例將被用於通過服務發現機制（如Netflix Eureka）調用註冊在Eureka等註冊中心的服務。
	 * 這個註解會使得RestTemplate具有負載均衡的能力，可以根據服務名稱而不是具體的服務地址來調用服務。
	 * 這是在微服務架構中常見的用法，特別是在使用Spring Cloud時。
	 * */
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
}
