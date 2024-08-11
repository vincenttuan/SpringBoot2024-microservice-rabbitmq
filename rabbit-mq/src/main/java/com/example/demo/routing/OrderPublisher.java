package com.example.demo.routing;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * 用於發送訂單消息的服務。
 * 發送消息時指定路由鍵，以確保消息正確路由到目標隊列。
 */
@Service
public class OrderPublisher {
	
	private RabbitTemplate rabbitTemplate;
	
	public OrderPublisher(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	/**
	 * 發送訂單消息
	 * @param type  訂單類型(digital(數位), clothing(服裝), food(食品)) 就是 routingKey(路由鍵)
	 * @param order 訂單詳情
	*/
	public void publishOrder(String type, String order) {
		// 發送消息到 direct exchange
		rabbitTemplate.convertAndSend("orders.direct", type, order);
	}
}
