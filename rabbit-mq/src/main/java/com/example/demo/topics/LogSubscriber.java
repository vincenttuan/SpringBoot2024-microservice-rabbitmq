package com.example.demo.topics;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 用於接收日誌消息的訂閱者。
 */
@Component
public class LogSubscriber {
	
	@RabbitListener(queues = "logs.app")
	public void receiveAppLog(String message) {
		System.out.println("接收到 app log: " + message);
	}
	
	@RabbitListener(queues = "logs.database")
	public void receiveDatabaseLog(String message) {
		System.out.println("接收到 database log: " + message);
	}
	
}
