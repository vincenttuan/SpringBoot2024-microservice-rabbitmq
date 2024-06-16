package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.po.Customer;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.CustomerService;

@Controller
@RequestMapping("/customers") // 根路徑 
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	// 多筆查詢
	@GetMapping // 使用根路徑
	public ResponseEntity<ApiResponse<List<Customer>>> findAll() {
		List<Customer> customers = customerService.findAll();
		ApiResponse<List<Customer>> apiResponse = null;
		if(customers.isEmpty()) {
			apiResponse = new ApiResponse<List<Customer>>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<List<Customer>>(true, "資料筆數:" + customers.size(), customers);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 單筆查詢
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Customer>> findById(@PathVariable Integer id) {
		Optional<Customer> customerOpt = customerService.findById(id);
		ApiResponse<Customer> apiResponse = null;
		if(customerOpt.isEmpty()) {
			apiResponse = new ApiResponse<Customer>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<Customer>(true, "有資料", customerOpt.get());
		}
		return ResponseEntity.ok(apiResponse);
	}
	
}
