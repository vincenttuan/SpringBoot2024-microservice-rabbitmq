package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/orders")
@RestController
public class OrderController {
	
	@GetMapping
	public ResponseEntity<Map<String, List<String>>> findAllOrders() {
		Map<String, List<String>> orders = new LinkedHashMap<>(); // 訂單資訊
		List<String> products = null; // 透過 Feign 取得商品資料
		orders.put("A01", products);
		return ResponseEntity.ok(orders);
	}
	
}
