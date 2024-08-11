package com.example.demo.routing;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/******************************************************

							 				
	                         digital  +-------+
	      orders.direct    +--------->| Queue | -> C1 (orders.digital) 
	     +-------------+   | clothing +-------+
    P -> |  Exchange   |---+--------->| Queue | -> C2 (orders.clothing)
	     +-------------+   | food     +-------+
	                       +--------->| Queue | -> C3 (orders.food)
	                                  +-------+
	                          
路由鍵 digital 發配到 orders.digital 隊列中
路由鍵 clothing 發配到 orders.clothing 隊列中
路由鍵 food 發配到 orders.food 隊列中
*******************************************************/

/**
 * RabbitMQ 設定類，用於配置交換機和隊列。
 * DirectExchange: 用於將消息發送到匹配路由鍵的隊列。
 * 每個隊列與交換機綁定時指定一個路由鍵。
 */
@Configuration
public class RabbitMQConfig {
	
	// 配置一個 DirectExchange
	// DirectExchange 會根據路由鍵將消息發送到特定隊列中
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange("orders.direct");
	}
	
	// 配置一個數位產品的隊列(orders.digital)
	@Bean
	public Queue digitalQueue() {
		return new Queue("orders.digital");
	}
	
	// 配置一個服飾產品的隊列(orders.clothing)
	@Bean
	public Queue clothingQueue() {
		return new Queue("orders.clothing");
	}
	
	// 配置一個食品產品的隊列(orders.food)
	@Bean
	public Queue foodQueue() {
		return new Queue("orders.food");
	}
	
	// 將數位產品隊列(digitalQueue)綁定到直接交換機(directExchange) 透過路由鍵 "digital"
	@Bean
	public Binding bindingDigital(DirectExchange directExchange, Queue digitalQueue) {
		return BindingBuilder.bind(digitalQueue).to(directExchange).with("digital");
	}
	
	// 將服裝產品隊列(clothingQueue)綁定到直接交換機(directExchange) 透過路由鍵 "clothing"
	@Bean
	public Binding bindingClothing(DirectExchange directExchange, Queue clothingQueue) {
		return BindingBuilder.bind(clothingQueue).to(directExchange).with("clothing");
	}
	
	// 將食品產品隊列(foodQueue)綁定到直接交換機(directExchange) 透過路由鍵 "food"
	@Bean
	public Binding bindingFood(DirectExchange directExchange, Queue foodQueue) {
		return BindingBuilder.bind(foodQueue).to(directExchange).with("food");
	}
	
	
}
