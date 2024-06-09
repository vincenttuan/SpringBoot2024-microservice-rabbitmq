package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ProductDao;
import com.example.demo.model.po.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductDao productDao;
	
	// 查詢全部
	public List<Product> findAll() {
		return productDao.findAll();
	}
	
	// 查詢單筆
	public Optional<Product> findById(Integer id) {
		return productDao.findById(id);
	}
	
}
