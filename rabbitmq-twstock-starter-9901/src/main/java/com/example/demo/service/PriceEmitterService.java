package com.example.demo.service;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class PriceEmitterService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    // 今日大盤每五秒的指數資訊
    private final Map<String, String> twiiPrices = new ConcurrentHashMap<>();
    
    
    // 通过自动注入获取 Exchange
//    @Autowired
//    private DirectExchange stockExchange;
//    
    @Autowired
    private FanoutExchange stockExchange;
    
    // 存放股價資訊
    private final Map<String, List<String>> stockPrices = new ConcurrentHashMap<>();
    
    // 存放股價資訊(HHMM00)
    private final Map<String, LinkedHashMap<String, String>> stockHHMM00Prices = new ConcurrentHashMap<>();
    
    // 存放最新價格
    private Map<String, String> lastPriceMap = new ConcurrentHashMap<>();
    
    // 存放最新交易量
    private Map<String, String> lastVolumeMap = new ConcurrentHashMap<>();
    
    // 停止標誌
    public boolean stop = false;
    
    private static final String jsonFileFolder = "C:/stock_price";
    private static final String jsonFilePath = "C:/stock_price/price.json";
    
    // 取得報價
    //ContextRefreshedEvent 會在 Springboot 啟動後觸發
    @EventListener(ContextRefreshedEvent.class)
    public void emitPrices() {
    	// 啟動執行緒
    	new Thread(() -> {
    		emitPricesService();
    	}).start();
    }
    
    // 取得報價服務
    public void emitPricesService() {
        try {
        	String jsonStr = Files.readString(Paths.get(jsonFilePath));
            String[] jsonStrs = jsonStr.split("\n");

            for (String json : jsonStrs) {
            	// 判斷是否停止
            	if (stop) {
            		break;
            	}
            	
            	String message = json.trim();
            	
                if (message.length() < 10 || message.contains("\"symbol\":\"000000\"")) {
                    continue;
                }
                
                JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
                String symbol = (jsonObject.get("symbol") + "").replace("\"", "");
                String lastPrice = (jsonObject.get("lastPrice") + "").replace("null", "");
                String lastVolume = (jsonObject.get("lastVolume") + "").replace("null", "");
                String matchTime = (jsonObject.get("matchTime") + "").replace("\"", "");
                if(lastPrice.trim().length() == 0 || lastVolume.trim().length() == 0) {
                	String preLastPrice = lastPriceMap.get(symbol);
					if (preLastPrice != null) {
						lastPrice = preLastPrice;
					}
					String preLastVolume = lastVolumeMap.get(symbol);
                    if (preLastVolume != null) {
                    	lastVolume = preLastVolume;
                    }
                } else {
                	lastPriceMap.put(symbol, lastPrice);
                	lastVolumeMap.put(symbol, lastVolume);
                }
                
                // 將 lastPrice 與 lastVolume 回寫到 jsonObject 並轉成 message
                jsonObject.addProperty("lastPrice", lastPrice.length() == 0 ? null : lastPrice);
                jsonObject.addProperty("lastVolume", lastVolume.length() == 0 ? null : lastVolume);
                message = jsonObject.toString();
                
				if (stockExchange.getType().equals("fanout")) {
					// FanoutExchange
					rabbitTemplate.convertAndSend(stockExchange.getName(), "", message);
				} else {
					// DirectExchange
					String routingKey = "tw.stock";
					// 使用 RabbitTemplate 和 stockExchange 发送消息, fanoutExchange
					rabbitTemplate.convertAndSend(stockExchange.getName(), routingKey, message);
				}
                
                System.out.println("發送: " + message);
        		
                // 先確認 symbol 是否存在
        		if (!stockPrices.containsKey(symbol)) {
        			stockPrices.put(symbol, new CopyOnWriteArrayList<>());
        			stockHHMM00Prices.put(symbol, new LinkedHashMap<>());
        		}
                // 更新股票代碼對應的價格
        		stockPrices.get(symbol).add(message);
        		
        		// 取得 HHMM00
        		String matchTimeHHMM = matchTime.substring(0, 4) + "00";
        		jsonObject.addProperty("matchTime", matchTimeHHMM);
        		message = jsonObject.toString();
        		stockHHMM00Prices.get(symbol).put(matchTimeHHMM, message);
        		
                Thread.sleep(1);  
            }
        } catch (Exception e) {
            System.out.println("報價結束: " + e.getMessage());
        }
    }
    
    // 停止
	public void stop() {
		stop = true;
	}
    
	//ContextRefreshedEvent 會在 Springboot 啟動後觸發
    @EventListener(ContextRefreshedEvent.class)
	public void twii() throws IOException {
		// 取得今日大盤交易紀錄
    	String jsonFilePath = jsonFileFolder + "/MI_5MINS_0410_INDEX.csv";
		// 資料格式: 
		// 時間, 指數
		// 090000,15,836.50
		// 090005,15,736.50
		// 090010,15,636.50
		// 將時間當作 key, 指數當作 value, 放到 twiiPrices Map
		Files.lines(Paths.get(jsonFilePath)).forEach(line -> {
			String[] arr = line.split(",");
			twiiPrices.put(arr[0], arr[1]);
		});
		System.out.println("取得今日大盤交易紀錄:" + twiiPrices.size());
	}
	
	// 取得最新價格
    public String getLastPrice(String symbol) {
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
	
	// 取得該 symbol HHMM 所有價格
	public Map<String, String> getHHMM00Prices(String symbol) {
		return stockHHMM00Prices.get(symbol);
	}
    
	// 取得所有價格
	public Map<String,List<String>> getAllPrices() {
		return stockPrices;
	}
    
}


