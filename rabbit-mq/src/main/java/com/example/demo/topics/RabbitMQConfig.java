package com.example.demo.topics;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
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
	
	@Bean
	public Queue appQueue() {
		return new Queue("logs.app");
	}
	
	@Bean
	public Queue databaseQueue() {
		return new Queue("logs.database");
	}
	
	@Bean
	public Binding bindingApp(TopicExchange topicExchange, Queue appQueue) {
		return BindingBuilder.bind(appQueue).to(topicExchange).with("app.*");
	}
	
	@Bean
	public Binding bindingDatabase(TopicExchange topicExchange, Queue databaseQueue) {
		return BindingBuilder.bind(databaseQueue).to(topicExchange).with("database.*");
	}
}
