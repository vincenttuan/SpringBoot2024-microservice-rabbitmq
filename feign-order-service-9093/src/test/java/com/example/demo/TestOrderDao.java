package com.example.demo;

import com.example.demo.dao.OrderDao;

public class TestOrderDao {
	public static void main(String[] args) {
		
		OrderDao orderDao = new OrderDao();
		orderDao.findAll().forEach(System.out::println);
		System.out.println();
		System.out.println(orderDao.findById(1).get());
	}
}
