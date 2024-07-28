package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {
	
	@GetMapping
	public ResponseEntity<List<String>> findAllProducts() {
		// 透過 List.of 所生成的集合是 ImmutableCollections(不可變集合)
		List<String> products = List.of("Shoes", "Laptop", "Mobile", "Pen", "Ball");
		return ResponseEntity.ok(products);
	}
	
}
