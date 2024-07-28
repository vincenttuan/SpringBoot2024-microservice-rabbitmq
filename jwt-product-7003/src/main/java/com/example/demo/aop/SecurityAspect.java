package com.example.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
@Aspect
public class SecurityAspect {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Pointcut("execution(* com.example.demo.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void authMethods() {
    }
	
	@Before("authMethods()")
	public void beforeMethod(JoinPoint joinPoint) {
		// 1.從 RequestContextHolder 中取得當前的 HttpServletRequest
		// 2.從 HttpServletRequest 中獲取 Authorization 的 Header 資料
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String authHeader = request.getHeader("Authorization");
		
		// 取得 token
		String token = authHeader.substring("Bearer ".length()); // 移除前面的 "Bearer " 綴字
		// 設定驗證路徑
		String verifyUrl = "http://JWT-SERVER-7001/verifyJwt?token=" + token;
		// 發送請求到 JWT-SERVER-7001 進行驗證
		ResponseEntity<String> response = restTemplate.getForEntity(verifyUrl, String.class);
		// 驗證 token
		if(response.getStatusCode().is2xxSuccessful()) {
			System.out.println("Token 驗證成功");
		} else {
			System.out.println("Token 驗證失敗");
			throw new SecurityException("Token 驗證失敗");
		}
		
	}
	
}
