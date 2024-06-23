package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ServiceCController {
	
	@GetMapping("/service-c/{name}")
	public String getResponse(@PathVariable String name, HttpServletRequest request) {
		System.out.printf("ServiceC name: %s port:%d%n", name, request.getLocalPort());
		
		return String.format("ServiceC name: %s port:%d%n", name, request.getLocalPort());
	}
}
