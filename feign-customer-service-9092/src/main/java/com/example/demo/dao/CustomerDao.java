package com.example.demo.dao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.example.demo.model.po.Customer;

@Repository
public class CustomerDao {
	
	private static List<Customer> customers;
	
	static {
		customers = new CopyOnWriteArrayList<>();
		customers.add(new Customer(1, "Alice", "alice@gmail.com"));
		customers.add(new Customer(2, "Bob", "bob@gmail.com"));
		customers.add(new Customer(3, "Cathy", "cathy@gmail.com"));
	}
	
	// 查詢全部
	public List<Customer> findAll() {
		return customers;
	}
	
	// 查詢單筆
	public Optional<Customer> findById(Integer id) {
		return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
	}
	
}
