package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.StockPriceService;

@CrossOrigin
@RestController
public class StockPriceController {
	
	@Autowired
	private StockPriceService stockPriceService;
	
	@GetMapping("/price/{symbol}")
	public String getLastPrice(@PathVariable String symbol) {
		return stockPriceService.getLatestPrice(symbol);
	}
	
	@GetMapping("/pre_price/{symbol}")
	public String getPrePrice(@PathVariable String symbol) {
		return stockPriceService.getPrePrice(symbol);
	}
	
	@GetMapping("/pre_prices")
	public Map<String, String> getPrePrices() {
		return stockPriceService.getPrePrices();
	}
	
}
