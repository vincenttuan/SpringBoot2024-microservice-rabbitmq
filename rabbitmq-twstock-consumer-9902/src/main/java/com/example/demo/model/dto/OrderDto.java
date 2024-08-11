package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	// 代號, 均價, 部位, 損益
	private String symbol;
	private Double avgPrice;
	private Integer amount;
	private Double profit;
	
	public OrderDto(String symbol, Double avgPrice, Integer amount) {
		this.symbol = symbol;
		this.avgPrice = avgPrice;
		this.amount = amount;
	}
	
	
}
