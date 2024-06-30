package com.example.demo;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Resilience4jProducer6060ApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void bulkheadTest() {
		for(int i=0;i<100;i++) {
			new Thread(() -> {
				try {
					//URL url = new URL("http://localhost:6060/employee/semaphore/1");
					URL url = new URL("http://localhost:6060/employee/threadpool/1");
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					int responseCode = conn.getResponseCode();
					System.out.printf("Response Code: %d%n", responseCode);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

}
