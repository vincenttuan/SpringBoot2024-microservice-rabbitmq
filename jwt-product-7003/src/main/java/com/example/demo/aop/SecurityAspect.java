package com.example.demo.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Aspect
public class SecurityAspect {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	
}
