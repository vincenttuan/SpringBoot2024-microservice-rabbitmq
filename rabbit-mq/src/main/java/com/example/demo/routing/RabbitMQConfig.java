package com.example.demo.routing;

import org.springframework.amqp.core.DirectExchange;
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
	
}
