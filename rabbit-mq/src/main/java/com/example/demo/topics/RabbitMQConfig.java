package com.example.demo.topics;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 設定類，用於配置交換機和隊列。
 * TopicExchange: 用於將消息根據主題路由鍵發送到匹配的隊列。
 * 使用 # 表示匹配多個單詞，* 表示匹配一個單詞。
 */
@Configuration
public class RabbitMQConfig {
	
	// TopicExchange 根據主題路由鍵將消息發送到匹配的隊列。
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange("logs.topic");
	}
	
}
