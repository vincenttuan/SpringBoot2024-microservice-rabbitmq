package com.example.demo.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item { // 訂單明細
	
	private Integer id; // 訂單項目編號
	private Integer orderId; // 訂單主檔編號
	private Integer productId; // 商品編號
	private Integer quantity; // 訂購數量
	private Integer subtotal; // 小計(商品價格 * 訂購數量) 
	
}
