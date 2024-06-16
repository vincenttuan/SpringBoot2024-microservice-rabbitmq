package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.po.Product;
import com.example.demo.model.response.ApiResponse;

//FEIGN-PRODUCT-SERVICE-9091 指的是註冊在 eureka 上的名字也就是應用名稱 "feign-product-service-9091"
@FeignClient(name = "FEIGN-PRODUCT-SERVICE-9091")
public interface ProductClient {
	
	@GetMapping("/products/{id}")
	public ApiResponse<Product> getProductById(@PathVariable("id") Integer id);
}
