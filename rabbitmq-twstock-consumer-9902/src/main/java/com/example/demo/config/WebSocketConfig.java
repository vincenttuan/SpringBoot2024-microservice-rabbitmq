package com.example.demo.config;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	// 消息代理的配置
	// configureMessageBroker方法配置了一個消息代理，以便在客戶端通過訂閱的方式接收服務器推送過來的消息。
	// 這裡配置了一個簡單的消息代理，以/topic為前綴來標識訂閱的消息。
	// 這樣客戶端只需要訂閱/topic/xxx這樣的地址就可以接收服務器推送過來的消息。
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic", "/queue");
		config.setApplicationDestinationPrefixes("/app");
		//config.setUserDestinationPrefix("/user");
	}
	
	// 端點的註冊
	// registerStompEndpoints方法註冊了一個STOMP端點/spring-boot-tutorial，
	// 客戶端將使用這個端點來連接WebSocket服務器。這個端點是STOMP客戶端與服務器進行交互的起點。
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/spring-boot-websocket").setAllowedOrigins("*");
	}
	
	@Bean(name = "passAllSSLRestTemplate")
	public RestTemplate createPassAllSSLRestTemplate() {
	    try {
	        // 信任所有憑證的 Trust Manager
	        TrustManager[] trustAllCerts = new TrustManager[] {
	            new X509TrustManager() {
	                public X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
	                public void checkClientTrusted(X509Certificate[] certs, String authType) {
	                }
	                public void checkServerTrusted(X509Certificate[] certs, String authType) {
	                }
	            }
	        };

	        // 忽略憑證的 Hostname Verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };

	        // 初始化 SSL Context
	        SSLContext sslContext = SSLContext.getInstance("TLS");
	        sslContext.init(null, trustAllCerts, new SecureRandom());

	        // 創建 RestTemplate
	        RestTemplate restTemplate = new RestTemplate();
	        SimpleClientHttpRequestFactory requestFactory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
	        requestFactory.setBufferRequestBody(false);
	        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

	        return restTemplate;

	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}