package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	// FanoutExchange
	@Bean
	@Qualifier("stockExchange")
	public FanoutExchange stockExchange() {
		return new FanoutExchange("stock_fanout_exchange");
	}
	
	// 定義 Queue：stockQueue
    @Bean
	public Queue stockQueue() {
		return QueueBuilder.durable("stock_queue").build();
	}
	
    @Bean
	public Binding stockBinding() {
		return BindingBuilder.bind(stockQueue()).to(stockExchange());
	}
	
    
    @Bean
 	@Qualifier("orderExchange")
 	public FanoutExchange orderExchange() {
 		return new FanoutExchange("order_fanout_exchange");
 	}
    
	
 	@Bean
 	@Qualifier("replyExchange")
 	public FanoutExchange replyExchange() {
 		return new FanoutExchange("reply_fanout_exchange");
 	}
 	
	// 定義 Queue：replyQueue
    @Bean
	public Queue replyQueue() {
		return QueueBuilder.durable("reply_queue").build();
	}
 	
	@Bean
	public Binding replyBinding() {
		return BindingBuilder.bind(replyQueue()).to(replyExchange());
	}
	
	
	
	/*
	// DirectExchange
    @Bean
	public DirectExchange stockExchange() {
		return new DirectExchange("stock_direct_exchange");
	}
    
    // 定義 routing key：tw.stock
    @Bean
	public Binding binding() {
		return BindingBuilder.bind(stockQueue()).to(stockExchange()).with("tw.stock");
	}
	*/
}
