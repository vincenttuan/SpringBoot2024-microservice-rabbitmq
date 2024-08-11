package com.example.demo.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class OrderMatcherService {
	
	@Autowired
    private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private FanoutExchange replyExchange;
	
	@Autowired
	private PriceEmitterService priceEmitterService;
	
	private String previousOrderNo;
	
    public OrderMatcherService() {
    	// 讀 orderNo.txt 檔案, 若無檔案則建立檔案並寫入 0000
    	try {
    		File file = new File("orderNo.txt");
			if (!file.exists()) {
				Files.createFile(Paths.get("orderNo.txt"));
				Files.writeString(Paths.get("orderNo.txt"), "0000");
			}
			previousOrderNo = Files.readString(Paths.get("orderNo.txt"));
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
    
	// 接收 Order 資訊
    @RabbitListener(queues = "order_queue")
    public void receiveOrder(String message) {
    	System.out.println("Received: " + message);
    	new Match(message).start();
    }
    
    private String getUniqueOrderNo() {
		int newOrderNo = Integer.parseInt(previousOrderNo) + 1;
		String orderNo = String.format("%04d", newOrderNo);
		previousOrderNo = orderNo;
		// 回寫
		try {
            Files.writeString(Paths.get("orderNo.txt"), orderNo);
        } catch (Exception e) {
        	
        }
		return orderNo;
	}
	
    // 搓合執行緒
	class Match extends Thread {
		String orderString;

		public Match(String orderString) {
			this.orderString = orderString;
		}

		@Override
		// 交易搓合
	 	public void run() {
			try {
				Thread.sleep(new Random().nextInt(100));
			} catch (Exception e) {
				
			}
	 		JsonObject orderObject = JsonParser.parseString(orderString).getAsJsonObject();
	 		String symbol = orderObject.get("symbol").getAsString();
	 		String bs = orderObject.get("bs").getAsString();
	 		Double price = orderObject.get("price").getAsDouble();
	 		Integer amount = orderObject.get("amount").getAsInt();
	 		
	 		JsonObject marketObject = JsonParser.parseString(priceEmitterService.getLastPrice(symbol)).getAsJsonObject();
	 		// "bidPrices":[21.8300,21.8100,21.8000,21.6900,21.6800], "askPrices":[21.9800,21.9900,22.100,22.200,22.2500]
	 		Boolean status = false;
	 		Double matchPrice = 0.0;
	 		Integer matchAmount = amount;
	 		if(bs.equals("B")) {
	 			Double bestAsk = marketObject.get("askPrices").getAsJsonArray().get(0).getAsDouble();
	 			if (price >= bestAsk) {
	 				matchPrice = bestAsk;
	 				status = true;
	 			}
	 		} else if(bs.equals("S")) {
	 			Double bestBid = marketObject.get("bidPrices").getAsJsonArray().get(0).getAsDouble();
	 			if (price <= bestBid) {
	 				matchPrice = bestBid;
	 				status = true;
	 			}
	 		}
	 		
	    	String replyMessage = String.format("{\"orderNo\":\"%s\",\"status\":\"%s\",\"symbol\":\"%s\", \"bs\":\"%s\", \"price\":\"%s\", \"amount\":\"%s\", \"matchPrice\":\"%s\", \"matchAmount\":\"%s\"}", 
	 				getUniqueOrderNo(), status, symbol, bs, price, amount, matchPrice, matchAmount);
	 		
	    	rabbitTemplate.convertAndSend(replyExchange.getName(), "", replyMessage);
	    	System.out.println("Reply: " + replyMessage);
	 	}
	}
    
    
 	
}
