package com.example.demo.routing;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 用於接收訂單消息的訂閱者
 */
@Component
public class OrderSubscriber {
	
	// 監聽數位商品隊列
	@RabbitListener(queues = "orders.digital")
	public void receiveDigitalOrder(String order) {
		System.out.println("接收到數位商品訂單: " + order);
	}
	
	// 監聽服飾商品隊列
	@RabbitListener(queues = "orders.clothing")
	public void receiveClothingOrder(String order) {
		System.out.println("接收到服飾商品訂單: " + order);
	}
	
	// 監聽食品商品隊列
	@RabbitListener(queues = "orders.food")
	public void receiveFoodOrder(String order) {
		System.out.println("接收到食品商品訂單: " + order);
	}
}
