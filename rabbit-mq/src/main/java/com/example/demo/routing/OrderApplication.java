package com.example.demo.routing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderApplication implements CommandLineRunner {
	
	private OrderPublisher orderPublisher;
	
	public OrderApplication(OrderPublisher orderPublisher) {
		this.orderPublisher = orderPublisher;
	}

	@Override
	public void run(String... args) throws Exception {
		orderPublisher.publishOrder("digital", "Order #1: 筆電一台");
		orderPublisher.publishOrder("clothing", "Order #2: T-shirt一件");
		orderPublisher.publishOrder("food", "Order #3: Pizza");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
	
}
