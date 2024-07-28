package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

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
	
	@Bean
	@RequestScope
	public TokenHolder tokenHolder() {
		return new TokenHolder();
	}
	
	// 定義一個 Token 的 Holder
	public static class TokenHolder {
		private String jwtToken;

		public String getJwtToken() {
			return jwtToken;
		}

		public void setJwtToken(String jwtToken) {
			this.jwtToken = jwtToken;
		}
		
	}
	
	
}
