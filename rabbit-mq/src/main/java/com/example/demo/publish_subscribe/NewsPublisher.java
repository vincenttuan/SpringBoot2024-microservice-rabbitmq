package com.example.demo.publish_subscribe;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * 用於發送新聞消息的服務。
 * RabbitTemplate: 提供了許多便捷的方法來發送和接收消息。
 * convertAndSend: 發送消息到指定的交換機。
 */
@Service
public class NewsPublisher {
	
	private RabbitTemplate rabbitTemplate;
	
	public NewsPublisher(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	// 發送新聞到 FanoutExchange
	public void publishNews(String news) {
		String routingKey = ""; // FanoutExchange 因為是廣播所以不需要設定路由鍵
		rabbitTemplate.convertAndSend("new.fanout", routingKey, news);
	}
	
}
