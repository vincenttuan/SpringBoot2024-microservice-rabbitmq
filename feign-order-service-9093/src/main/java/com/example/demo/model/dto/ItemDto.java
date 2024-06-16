package com.example.demo.model.dto;

import com.example.demo.model.po.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
	private Integer id; // 訂單項目編號
	private Product product; // 商品資料
	private Integer quantity; // 訂購數量
	private Integer subtotal; // 小計(商品價格 * 訂購數量)
}
