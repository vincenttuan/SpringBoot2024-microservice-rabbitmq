package com.example.demo.routing;

import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 設定類，用於配置交換機和隊列。
 * TopicExchange: 用於將消息根據主題路由鍵發送到匹配的隊列。
 * 使用 # 表示匹配多個單詞，* 表示匹配一個單詞。
 */
@Configuration
public class RabbitMQConfig {

}
