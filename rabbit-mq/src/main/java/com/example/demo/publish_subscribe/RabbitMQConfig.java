package com.example.demo.publish_subscribe;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;


/******************************************************
							 news.website				
	                          +-------+
	       news.fanout     +--| Queue | -> C1 網站接收新聞
	     +-------------+   |  +-------+
P發新聞 -> |  Exchange   |---+
	     +-------------+   |  +-------+
	                       +--| Queue | -> C2 App接收新聞
	                          +-------+
	                          news.app
 
 * RabbitMQ 設定類，用於配置交換機和隊列。
 * FanoutExchange: 用於將消息廣播到所有綁定的隊列。
 * Queue: 用於緩存消息，等待消費者消費。
 * Binding: 將隊列綁定到交換機的配置。
*******************************************************/
// RabbitMQ 設定類，用於配置交換機和隊列。
public class RabbitMQConfig {
	
	// FanoutExchange: 用於將消息廣播到所有綁定的隊列。
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange("news.fanout");
	}
	
	// Queue: 用於緩存消息，等待消費者消費。
	@Bean
	public Queue websiteQueue() {
		return new Queue("news.website");
	}
	
	@Bean
	public Queue appQueue() {
		return new Queue("news.app");
	}
	
	
	
	
}
