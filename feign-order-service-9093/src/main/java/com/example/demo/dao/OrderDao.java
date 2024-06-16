package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.example.demo.model.po.Item;
import com.example.demo.model.po.Order;

@Repository
public class OrderDao {
	
	private static List<Order> orders;
	
	static {
		orders = new CopyOnWriteArrayList<>();
		// 訂單編號, 客戶編號, 訂單日期, 總計, 訂單明細
		Order order1 = new Order(1, 2, "2024-06-01", 0, new ArrayList<>(List.of(
				// 訂單項目編號, 訂單主檔編號, 商品編號, 訂購數量, 小計
				new Item(1, 1, 1, 5, 0), new Item(2, 1, 3, 2, 0))));
		
		Order order2 = new Order(2, 1, "2024-06-02", 0, new ArrayList<>(List.of(
				// 訂單項目編號, 訂單主檔編號, 商品編號, 訂購數量, 小計
				new Item(1, 2, 2, 4, 0), new Item(2, 2, 3, 1, 0))));
		
		Order order3 = new Order(3, 3, "2024-06-03", 0, new ArrayList<>(List.of(
				// 訂單項目編號, 訂單主檔編號, 商品編號, 訂購數量, 小計
				new Item(1, 3, 1, 2, 0), new Item(2, 3, 2, 4, 0), new Item(3, 3, 3, 1, 0))));
		
		Order order4 = new Order(4, 3, "2024-06-04", 0, new ArrayList<>(List.of(
				// 訂單項目編號, 訂單主檔編號, 商品編號, 訂購數量, 小計
				new Item(1, 4, 1, 7, 0))));
		
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		orders.add(order4);
	}
	
	// 查詢所有訂單
	public List<Order> findAll() {
		return orders;
	}
	
	// 查詢單筆
	public Optional<Order> findById(Integer id) {
		return orders.stream().filter(order -> order.getId().equals(id)).findFirst();
	}
	
}
