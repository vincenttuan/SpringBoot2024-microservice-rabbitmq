package com.example.demo.dao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.example.demo.model.po.Product;

@Repository
public class ProductDao {
	
	// 利用集合來儲存商品資料(In-Memory)
	private static List<Product> products;
	
	static {
		// 初始商品資料
		products = new CopyOnWriteArrayList<>();
		products.add(new Product(1, "Apple", 10, 4, 100));
		products.add(new Product(2, "Banana", 20, 8, 200));
		products.add(new Product(3, "Cherry", 30, 15, 300));
	}
	
	// 查詢全部
	public List<Product> findAll() {
		return products;
	}
	
	// 查詢單筆
	public Optional<Product> findById(Integer id) {
		return products.stream().filter(product -> product.getId().equals(id)).findFirst();
	}
	
}
