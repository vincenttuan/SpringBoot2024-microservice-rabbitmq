package com.example.demo.model.po;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order { // 訂單主檔
	private Integer id; // 訂單編號
	private Integer customerId; // 客戶編號
	private String orderDate; // 訂單日期
	private Integer total; // 總計
	private List<Item> items = new CopyOnWriteArrayList<>(); // 訂單明細
}
