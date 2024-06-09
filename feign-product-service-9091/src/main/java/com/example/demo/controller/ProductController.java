package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.po.Product;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/products") // 根路徑
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	// 多筆查詢
	@GetMapping // 使用根路徑
	public ResponseEntity<ApiResponse<List<Product>>> findAll() {
		List<Product> products = productService.findAll();
		ApiResponse<List<Product>> apiResponse = null;
		if(products.isEmpty()) {
			apiResponse = new ApiResponse<List<Product>>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<List<Product>>(true, "資料筆數:" + products.size(), products);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 單筆查詢
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> findById(@PathVariable Integer id) {
		Optional<Product> productOpt = productService.findById(id);
		ApiResponse<Product> apiResponse = null;
		if(productOpt.isEmpty()) {
			apiResponse = new ApiResponse<Product>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<Product>(true, "有資料", productOpt.get());
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	
}
