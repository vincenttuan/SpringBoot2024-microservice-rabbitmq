package com.example.demo.publish_subscribe;

import java.util.Date;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 用於接收新聞消息的訂閱者。
 * 使用 @RabbitListener 註解來實現消息接收：
 * @RabbitListener: 註冊消息監聽器並指定需要監聽的隊列。
 */
@Service
public class NewsSubscriber {
	
	// 從 news.website 隊列中接收到新聞消息(監聽 news.website 隊列)
	@RabbitListener(queues = "news.website")
	public void reveiveFromWebsite(String news) {
		System.out.println("Website 收到: " + news + " " + new Date());
	}
	
	// 從 news.app 隊列中接收到新聞消息(監聽 news.app 隊列)
	@RabbitListener(queues = "news.website")
	public void reveiveFromApp(String news) {
		System.out.println("App 收到: " + news + " " + new Date());
	}
	
}
