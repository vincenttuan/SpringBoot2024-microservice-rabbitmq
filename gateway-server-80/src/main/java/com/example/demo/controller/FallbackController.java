package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
	
	@GetMapping("/fallback")
	public String fallback() {
		return "此服務暫停不可使用, 請稍後再試!";
	}
}
