package com.example.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.po.Order;
import com.example.demo.repository.OrderRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class OrderReplyService {
	
	@Autowired
	@Qualifier("orderExchange")
	private FanoutExchange orderExchange;
	
	@Autowired
	@Qualifier("replyExchange")
	private FanoutExchange replyExchange;
	
	@Autowired
    private RabbitTemplate rabbitTemplate;
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
    private OrderRepository orderRepository;

    
	// 下單
	public void placeOrder(String orderString) {
		System.out.println("OrderService 下單: " + orderString);
		rabbitTemplate.convertAndSend(orderExchange.getName(), "", orderString);
	}
	
	// 回覆下單結果
	@RabbitListener(queues = "reply_queue")
	public void replyOrder(String replyString) {
		System.out.println("OrderService 接到回覆: " + replyString);
		saveOrderFromJson(replyString);
		messagingTemplate.convertAndSend("/topic/reply", replyString);
	}
	
	public List<OrderDto> findAllOrderDto() {
	    List<Object[]> results = orderRepository.findAllOrderDto();
	    List<OrderDto> dtos = new ArrayList<>();
	    for (Object[] result : results) {
	        dtos.add(new OrderDto(
	            (String) result[0], // 假設第一個元素是字符串
	            (Double) result[1], // 假設第二個元素是雙精度浮點
	            ((BigDecimal) result[2]).intValue() // 將 BigDecimal 轉為 int
	        ));
	    }
	    return dtos;
	}

	
	public void saveOrderFromJson(String json) {
		new Thread(() -> {
			// 解析 JSON 字符串並創建 Order 對象
			JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
			String symbol = jsonObject.get("symbol").getAsString();
			int amount = jsonObject.get("amount").getAsInt();
			String bs = jsonObject.get("bs").getAsString();
			
			if(bs.equals("B")) {
				for(int i = 0; i < amount; i++) {
					Order order = new Order();
					order.setOrderNo(jsonObject.get("orderNo").getAsString());
					order.setStatus(jsonObject.get("status").getAsString());
					order.setSymbol(symbol);
					order.setBs(bs);
					order.setPrice(jsonObject.get("price").getAsDouble());
					order.setAmount(1); // 固定放一
					order.setMatchPrice(jsonObject.get("matchPrice").getAsDouble());
					order.setMatchAmount(1);
					// 保存 Order 對象到數據庫
			        orderRepository.save(order);
				}
			} else if(bs.equals("S")) {
				// 刪除 amount 筆 symbol 欄位的內容為 symbol 且 bs 為 B 的 Order 資料庫紀錄
				int deleteCount = 0;
				for (int i = 0; i < amount; i++) {
					deleteCount += orderRepository.deleteSingleOrderBySymbol(symbol);
				}
				if (deleteCount == 0) {
					System.out.println("Orders 中沒有資料可以刪除");
					return;
				}
				Order order = new Order();
				order.setOrderNo(jsonObject.get("orderNo").getAsString());
				order.setStatus(jsonObject.get("status").getAsString());
				order.setSymbol(symbol);
				order.setBs(bs);
				order.setPrice(jsonObject.get("price").getAsDouble());
				order.setAmount(amount);
				order.setMatchPrice(jsonObject.get("matchPrice").getAsDouble());
				order.setMatchAmount(jsonObject.get("matchAmount").getAsInt());
				// 保存 Order 對象到數據庫
				orderRepository.save(order);
				
			}
	        System.out.println("OrderService 保存 Order 對象到數據庫");
		}).start();
    }
}
