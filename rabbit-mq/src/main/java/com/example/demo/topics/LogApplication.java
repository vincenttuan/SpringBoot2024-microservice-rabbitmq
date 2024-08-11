package com.example.demo.topics;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogApplication implements CommandLineRunner {
	
	private LogPublisher logPublisher;
	
	public LogApplication(LogPublisher logPublisher) {
		this.logPublisher = logPublisher;
	}
	
	@Override
	public void run(String... args) throws Exception {
		logPublisher.publishLog("app.info", "Application started successfully.");
		logPublisher.publishLog("app.error", "Null pointer exception occurred.");
		logPublisher.publishLog("database.update", "Updated user details.");
		logPublisher.publishLog("database.query.all", "Executed SELECT query.");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LogApplication.class, args);
	}

}
