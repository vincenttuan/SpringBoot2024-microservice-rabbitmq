package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
	
	// FanoutExchange
	@Bean
	public FanoutExchange stockExchange() {
		return new FanoutExchange("stock_fanout_exchange");
	}
	
    // DirectExchange
//    @Bean
//	public DirectExchange stockExchange() {
//		return new DirectExchange("stock_direct_exchange");
//	}
	
	@Bean
	public Queue orderQueue() {
		return QueueBuilder.durable("order_queue").build();
	}
	
	@Bean
	public FanoutExchange orderExchange() {
		return new FanoutExchange("order_fanout_exchange");
	}
	
	@Bean
	public Binding binding() {
		return BindingBuilder.bind(orderQueue()).to(orderExchange());
	}
	
	//-------------------------------------------------------------------
	
	@Bean
	public FanoutExchange replyExchange() {
		return new FanoutExchange("reply_fanout_exchange");
	}
    
}
