package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private EurekaClient eurekaClient;
	
	@GetMapping
	public String index() {
		return "Welcome page !";
	}
	
	@GetMapping("/down")
	public String down() {
		InstanceInfo instanceInfo = eurekaClient.getApplicationInfoManager().getInfo();
		instanceInfo.setStatus(InstanceInfo.InstanceStatus.DOWN);
		return "Welcome service: DOWN";
	}
	
	// Homework:
	// UP 請自行練習
	// 這邊所指的是 info 的片面改變
	// 若要改變 health 則必須要實作 HealthIndicator
	// 並回傳 Health.down()
}
