package com.example.demo.routing;

import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 設定類，用於配置交換機和隊列。
 * DirectExchange: 用於將消息發送到匹配路由鍵的隊列。
 * 每個隊列與交換機綁定時指定一個路由鍵。
 */
@Configuration
public class RabbitMQConfig {

}
