package com.example.demo.topics;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogApplication implements CommandLineRunner {
	
	private LogPublisher logPublisher;
	
	public LogApplication(LogPublisher logPublisher) {
		this.logPublisher = logPublisher;
	}
	
	@Override
	public void run(String... args) throws Exception {
		
	}
	
	

}
