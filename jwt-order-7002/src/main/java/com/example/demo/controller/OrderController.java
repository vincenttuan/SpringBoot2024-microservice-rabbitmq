package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.client.ProductClient;

@CrossOrigin
@RequestMapping("/orders")
@RestController
public class OrderController {
	
	@Autowired
	private ProductClient productClient;
	
	@GetMapping
	public ResponseEntity<Map<String, List<String>>> findAllOrders() {
		Map<String, List<String>> orders = new LinkedHashMap<>(); // 訂單資訊
		List<String> products = productClient.findAllProducts(); // 透過 Feign 取得商品資料
		orders.put("A01", products);
		return ResponseEntity.ok(orders);
	}
	
}
