package com.example.demo.model.po;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "orders") // 資料表會自動產生
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String orderNo;
    private String status;
    private String symbol;
    private String bs; // buy or sell
    private Double price;
    private Integer amount;
    private Double matchPrice;
    private Integer matchAmount;
}
