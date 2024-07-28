package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import feign.RequestInterceptor;

@Configuration
public class FeignClientConfig {
	
	// 定義一個請求攔截器
	@Bean
	public RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			// 取得 token
			String token = "";
			// 將 token 添加到 requestTemplate 的 header 中
			requestTemplate.header("Authorization", "Bearer " + token);
		};
	}
	
}
