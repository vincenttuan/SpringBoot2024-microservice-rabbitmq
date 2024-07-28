package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.demo.client")
public class JwtOrder7002Application {

	public static void main(String[] args) {
		SpringApplication.run(JwtOrder7002Application.class, args);
	}

}
