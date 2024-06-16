package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.OrderDao;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.po.Customer;
import com.example.demo.model.po.Order;

@Service
public class OrderService {
	@Autowired
	private OrderDao orderDao;
	
	// Order 單筆訂單 po 轉 dto
	private OrderDto convertToDto(Order order) {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(order.getId());
		orderDto.setOrderDate(order.getOrderDate());
		
		Customer customer = customerClient.getCustomerById(order.getId());
		orderDto.setCustomer(customer);
		
	}
	
	// 單筆訂單
	private OrderDto getOrderById(Integer orderId) {
		Optional<Order> orderOpt = orderDao.findById(orderId);
		if(orderOpt.isEmpty()) {
			return null;
		}
		Order order = orderOpt.get();
		// 將 po 轉 dto
		return convertToDto(order);
	}
	
}
