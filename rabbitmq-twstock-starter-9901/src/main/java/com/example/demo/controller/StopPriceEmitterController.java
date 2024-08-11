package com.example.demo.controller;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.PriceEmitterService;

@CrossOrigin
@RestController
public class StopPriceEmitterController {
	
	@Autowired
	private PriceEmitterService priceEmitterService;
	
	@GetMapping("/stop")
	public String stop() {
		priceEmitterService.stop();
		return "stop";
	}
	
	@GetMapping("/price/last/{symbol}")
	public String getLastPrice(@PathVariable String symbol) {
		return priceEmitterService.getLastPrice(symbol);
	}
	
	@GetMapping("/price/{symbol}")
	public List<String> getPrice(@PathVariable String symbol) {
		return priceEmitterService.getPrices(symbol);
	}
	
	@GetMapping("/price/hhmm00/{symbol}")
	public Collection<String> getHHMM00Price(@PathVariable String symbol) {
		return priceEmitterService.getHHMM00Prices(symbol).values();
	}
	
	@GetMapping("/prices")
	public Map<String,List<String>> getPrices() {
		return priceEmitterService.getAllPrices();
	}
	
	
}
