package com.example.demo.service;

import java.util.Optional;

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
		Customer customer = customerClient.getCustomerById(order.getId()).getData();
		orderDto.setCustomer(customer);
		
		// 透過 Feign 取得遠端商品資料
		for(Item item : order.getItems()) {
			// Item po 轉 dto
			ItemDto itemDto = convertToDto(item);
			orderDto.getItemDtos().add(itemDto);
		}
		
		return orderDto;
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
	
}
