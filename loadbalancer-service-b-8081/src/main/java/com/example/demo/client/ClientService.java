package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "loadbalancer-service-c-9001")
public interface ClientService {
	
	@GetMapping("/service-c/{name}")
	public String getResponse(@PathVariable String name);
}
