package com.example.demo.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.client.ClientService;
import com.example.demo.service.RetryService;

@RestController
public class ServiceBController {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private RetryService retryService;
	
	@GetMapping("/service-b/{name}")
	public String getResponse(@PathVariable String name) {
		return clientService.getResponse(name);
	}
	
	@GetMapping("/retry/service-b/{name}")
	public String getRetryResponse(@PathVariable String name) throws IOException, SQLException {
		return retryService.getRetryResponse(name);
	}
	
	
}
