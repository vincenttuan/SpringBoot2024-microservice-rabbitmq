package com.example.demo.publish_subscribe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 啟動新聞發布系統
@SpringBootApplication
public class NewsApplication implements CommandLineRunner {
	
	private NewsPublisher newsPublisher;
	
	public NewsApplication(NewsPublisher newsPublisher) {
		this.newsPublisher = newsPublisher;
	}

	@Override
	public void run(String... args) throws Exception {
		newsPublisher.publishNews("倫敦奧運-羽球金牌戰 10:00 線上直播");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(NewsApplication.class, args);
	}
	
	
	
}
