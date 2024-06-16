package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.po.Customer;
import com.example.demo.model.response.ApiResponse;

// FEIGN-CUSTOMER-SERVICE-9092 指的是註冊在 eureka 上的名字也就是應用名稱 "feign-customer-service-9092"
@FeignClient(name = "FEIGN-CUSTOMER-SERVICE-9092")
public interface CustomerClient {
	@GetMapping("/customers/{id}")
	ApiResponse<Customer> getCustomerById(@PathVariable("id") Integer id);
	
}
