package com.example.demo.model.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.demo.model.po.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	private Integer id; // 訂單編號
	private Customer customer; // 客戶資料
	private String orderDate; // 訂單日期
	private Integer total; // 總計
	private List<ItemDto> itemDtos = new CopyOnWriteArrayList<>();
	
}
