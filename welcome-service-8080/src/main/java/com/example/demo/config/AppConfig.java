package com.example.demo.config;

import java.util.Map;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	// 定義 /actuator/info 可以看到的資訊
	@Bean
	public InfoContributor appInfoContributor() {
		return (builder) -> {
			builder.withDetail("app", 
					Map.of(
						"name", "Welcome Service",
						"description", "用來顯示歡迎頁面的資訊",
						"version", "1.0.0"
					));
		}; 
	}
	
}
