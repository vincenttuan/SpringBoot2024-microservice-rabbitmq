package com.example.demo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "JWT-PRODUCT-7003")
public interface ProductClient {
	@GetMapping("/products")
	List<String> findAllProducts();
}
