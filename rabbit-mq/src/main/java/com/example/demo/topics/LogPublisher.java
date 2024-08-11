package com.example.demo.topics;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * 用於發送日誌消息的服務。
 * publishLog: 根據指定的路由鍵發送日誌消息。
 */
@Service
public class LogPublisher {
	
	private RabbitTemplate rabbitTemplate;
	
	public LogPublisher(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	/**
     * 發送日誌消息到 TopicExchange。
     *
     * @param routingKey 路由鍵，決定消息的路由方式(就是要傳到哪一個 Queue)
     * @param message    要發送的日誌消息
     */
	public void publishLog(String routingKey, String message) {
		// 發送消息到 topic exchange 根據主題, 路由鍵, 消息
		rabbitTemplate.convertAndSend("logs.topic", routingKey, message);
	}
}
