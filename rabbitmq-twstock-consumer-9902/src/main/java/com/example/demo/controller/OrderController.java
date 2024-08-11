package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.po.Order;
import com.example.demo.service.OrderReplyService;

@CrossOrigin
@RestController
public class OrderController {
	
	@Autowired
	private OrderReplyService orderService;
	
	@GetMapping("/orders")
	public List<OrderDto> getOrders() {
		return orderService.findAllOrderDto();
	}
	
	@PostMapping("/order")
	public String match(@RequestBody String orderString) {
		System.out.println("OrderController 下單: " + orderString);
		orderService.placeOrder(orderString);
		// 覆誦下單訊息
		return orderString; 
	}
}
