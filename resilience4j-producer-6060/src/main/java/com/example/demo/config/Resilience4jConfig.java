package com.example.demo.config;

import org.springframework.context.annotation.Configuration;

/**
 * Resilience4j 配置類，用於配置各種容錯機制如重試、限流、隔離和時間限制等。
 */
@Configuration
public class Resilience4jConfig {
	
    /**
     * 配置重試機制 (Retry)
     * 目的是確保服務在遇到臨時故障時能夠重試，從而提高服務的穩定性。
     * 運作原理是設置最大嘗試次數和重試間隔時間，在指定次數內重試請求。
     * 
     * maxAttempts(3): 表示在初始嘗試一次失敗後，重試將進行兩次，所以總共是三次。
     * waitDuration: 重試之間的等待時間為 500 毫秒。
     * 
     * @return RetryRegistry
     */
	
	
}
