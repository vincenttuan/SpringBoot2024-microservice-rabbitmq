package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.client.CustomerClient;
import com.example.demo.client.ProductClient;
import com.example.demo.dao.OrderDao;
import com.example.demo.model.dto.ItemDto;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.po.Customer;
import com.example.demo.model.po.Item;
import com.example.demo.model.po.Order;
import com.example.demo.model.po.Product;

@Service
public class OrderService {
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private CustomerClient customerClient;
	
	@Autowired
	private ProductClient productClient;
	
	// Item 單筆訂單項目 po 轉 dto
	private ItemDto convertToDto(Item item) {
		ItemDto itemDto = new ItemDto();
		itemDto.setId(item.getId());
		itemDto.setQuantity(item.getQuantity());
		
		// 透過 Feign 取得遠端商品資料
		Product product = productClient.getProductById(item.getProductId()).getData();
		itemDto.setProduct(product);
		
		return itemDto;
	}
	
	// Order 單筆訂單 po 轉 dto
	private OrderDto convertToDto(Order order) {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(order.getId());
		orderDto.setOrderDate(order.getOrderDate());
		
		// 透過 Feign 取得遠端客戶資料
		//Customer customer = customerClient.getCustomerById(order.getId()).getData();
		// 更正
		Customer customer = customerClient.getCustomerById(order.getCustomerId()).getData();
		
		orderDto.setCustomer(customer);
		
		// 透過 Feign 取得遠端商品資料
		for(Item item : order.getItems()) {
			// Item po 轉 dto
			ItemDto itemDto = convertToDto(item);
			orderDto.getItemDtos().add(itemDto);
		}
		
		return orderDto;
	}
	
	// Orders 多筆訂單 po 轉 dto
	private List<OrderDto> convertToDto(List<Order> orders) {
		//return orders.stream().map(order -> convertToDto(order)).collect(Collectors.toList());
		return orders.stream().map(this::convertToDto).collect(Collectors.toList());
	}
	
	// 單筆訂單
	public OrderDto getOrderById(Integer orderId) {
		Optional<Order> orderOpt = orderDao.findById(orderId);
		if(orderOpt.isEmpty()) {
			return null;
		}
		Order order = orderOpt.get();
		// 將 po 轉 dto
		return convertToDto(order);
	}
	
	// 所有訂單
	public List<OrderDto> findAll() {
		List<Order> orders = orderDao.findAll();
		// 將 List po 轉 List dto
		return convertToDto(orders);
	}
	
}
