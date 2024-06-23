package com.example.demo.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.example.demo.client.ClientService;

@Service
public class RetryService {
	
	@Autowired
	private ClientService clientService;
	
	/*
	 * @Retryable的參數介紹
		value：指定需要重試的異常類型。如果沒有指定，則默認重試所有異常。可以指定多個異常類型，以陣列的形式提供。
		maxAttempts：指定最多重試次數。默認值為3。
		backoff：指定重試間隔時間。可以指定一個@Backoff註釋對象，這個對象有兩個屬性：
		delay：指定重試間隔時間，默認為0毫秒。
		multiplier：指定每次重試的間隔時間增加倍數，默認為1。
		include：指定需要重試的異常類型。與value類似，但是是針對於某些異常類型進行重試。
		exclude：指定不需要重試的異常類型。與value類似，但是是針對於某些異常類型不進行重試。
	 * */
	@Retryable(value = IOException.class, maxAttempts = 4, backoff = @Backoff(delay = 3000L))
	public String getRetryResponse(String name) throws IOException {
		int randomValue = new Random().nextInt(100);
		if(randomValue < 80) {
			System.out.printf("Service B 發生例外! randomValue:%d%n", randomValue);
			throw new IOException(String.format("Service B 發生例外! randomValue:%d%n", randomValue));
		}
		System.out.println("Service B getRetryResponse");
		return clientService.getResponse(name);
	}
	
	@Recover
	public String testRecover(IOException e) {
		System.out.printf("因為 Retry 失敗: %s, 所以執行 Recover 的邏輯%n", e.getMessage());
		// block of code...
		
		return String.format("因為 Retry 失敗: %s, 所以執行 Recover 的邏輯%n", e.getMessage());
	}
	
	@Recover
	public String testRecover(SQLException e) {
		System.out.printf("因為 Retry 失敗: %s, 所以執行 Recover 的邏輯%n", e.getMessage());
		// block of code...
		
		return String.format("因為 Retry 失敗: %s, 所以執行 Recover 的邏輯%n", e.getMessage());
	}
	
	
}
