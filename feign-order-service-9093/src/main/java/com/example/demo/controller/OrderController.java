package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	// 查詢指定訂單
	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable Integer orderId) {
		OrderDto orderDto = orderService.getOrderById(orderId);
		ApiResponse<OrderDto> apiResponse = null;
		if(orderDto == null) {
			apiResponse = new ApiResponse<>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<>(true, "有資料", orderDto);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
}
