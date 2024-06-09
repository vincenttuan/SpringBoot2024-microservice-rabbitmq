package com.example.demo.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product { // 商品
	private Integer id; // 商品序號
	private String name; // 商品名稱
	private Integer price; // 商品價格
	private Integer cost; // 商品成本
	private Integer quantity; // 商品數量
}
