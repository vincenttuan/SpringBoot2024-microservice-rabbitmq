package com.example.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 股價服務
@Service
public class StockPriceService {
	// 股價資訊
    private final Map<String, List<String>> stockPrices = new ConcurrentHashMap<>();
    
    // 上一個交易日股價資訊
    private final Map<String, String> stockPrePrices = new ConcurrentHashMap<>();
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    // 接收股價資訊
    @RabbitListener(queues = "stock_queue")
    public void receiveStockPrice(String message) {
    	System.out.println("Received: " + message);
    	// 取得 symbol
    	JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
		String symbol = jsonObject.get("symbol").getAsString().trim();
		// 先確認 symbol 是否存在
		if (!stockPrices.containsKey(symbol)) {
			stockPrices.put(symbol, new ArrayList<>());
		}
        // 更新股票代碼對應的價格
		stockPrices.get(symbol).add(message);
		
		messagingTemplate.convertAndSend("/topic/all", message);
		messagingTemplate.convertAndSend("/topic/" + symbol, message);
	}
    
    // 取得最新價格
    public String getLatestPrice(String symbol) {
        List<String> list = stockPrices.get(symbol) == null ? new ArrayList<>() : stockPrices.get(symbol);
		if (list.isEmpty()) {
			return symbol + " no price available";
		}
        return list.get(list.size() - 1);
    }
    
    // 取得該 symbol 所有價格
	public List<String> getPrices(String symbol) {
		return stockPrices.get(symbol);
	}
    
	// 取得所有價格
	public Map<String,List<String>> getAllPrices() {
		return stockPrices;
	}
	
	// 取得上一個交易日所有股票交易紀錄
	public Map<String, String> getPrePrices() {
		return stockPrePrices;
	}
	
	// 取得上一個交易日個股股票交易紀錄
	public String getPrePrice(String symbol) {
		return getPrePrices().get(symbol);
	}
	
	//ContextRefreshedEvent 會在 Springboot 啟動後觸發
    @EventListener(ContextRefreshedEvent.class)
    public void makePrePriceMap() throws Exception {
    	String jsonFileFolder = null;
    	// 判斷作業系統是否是 Windows
    	if (System.getProperty("os.name").toLowerCase().contains("windows")) {
    		jsonFileFolder = "C:/Users/vince/Downloads";
    	} else {
    		// mac os
    		jsonFileFolder = "/Volumes/SSD/data";
    	}
    	
    	String jsonFilePath = jsonFileFolder + "/pre_price_0407.json"; // json array 資訊
    	System.out.println("取得上一個交易日所有股票交易紀錄 => " + jsonFilePath);
    	// 存放在 Map<String, String>, key=symbol, value=json string
    	JsonArray jsonArray = JsonParser.parseString(Files.readString(Paths.get(jsonFilePath))).getAsJsonArray();
    	// 用 stream 存放在 Map 集合
		jsonArray.forEach(jsonElement -> {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			// jsonObject 內容裡面有 , 或 + 都要去掉
			for (String key : jsonObject.keySet()) {
				jsonObject.addProperty(key, jsonObject.get(key).getAsString().replace(",", "").replace("+", ""));
			}
			
			String symbol = jsonObject.get("symbol").getAsString().trim();
			stockPrePrices.put(symbol, jsonObject.toString());
		});
		System.out.println("取得上一個交易日所有股票交易紀錄筆數 => " + stockPrePrices.size());
		
    }
}

