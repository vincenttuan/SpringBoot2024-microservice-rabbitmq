package com.example.demo;

import java.util.List;

import com.example.demo.dao.OrderDao;
import com.example.demo.model.po.Order;

public class TestOrderDao {
	public static void main(String[] args) {
		
		OrderDao orderDao = new OrderDao();
		orderDao.findAll().forEach(System.out::println);
		System.out.println();
		System.out.println(orderDao.findById(1).get());
	}
}
