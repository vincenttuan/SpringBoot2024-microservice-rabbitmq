package com.example.demo;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SimpleService80Application {

	public static void main(String[] args) {
		SpringApplication.run(SimpleService80Application.class, args);
	}

	@GetMapping("/today")
	public String today() {
		return "today " + new Date();
	}
}
