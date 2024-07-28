package com.example.demo.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.JWTServiceNimbus;
import com.nimbusds.jose.JOSEException;

//Access-Control-Allow-Origin
@CrossOrigin
@RestController
public class JWTController {
	
	@Autowired
	private JWTServiceNimbus jwtServiceNimbus;
	
	private static Map<String, String> tokens = new ConcurrentHashMap<>();
	
	private static Map<String, String> users = Map.of("john", "1234", "mary", "5678", "admin", "1111");
	
	/**
     * 申請一個訪客 JWT
     * @return ResponseEntity 包含訪客 JWT 的字串
     * @throws JOSEException 當 JWT 創建失敗時拋出
     */
	@GetMapping("/guestJWT")
	public ResponseEntity<String> getGuestJWT() throws JOSEException {
		String guestJWT = jwtServiceNimbus.createToken("guest", "user", 600_000);
		return ResponseEntity.ok(guestJWT);
	}
	
	/**
     * 申請一個正式 JWT
     * Header: User-Agent: 使用者名字 (Ex: john)
     *         Service-Identifier: 服務 ID (Ex: report)
     *         Authorization: Basic 使用者帳密
     * @param userAgent 使用者名字
     * @param serviceId 服務 ID
     * @param authorizationHeader Basic 認證的 Header
     * @return ResponseEntity 包含正式 JWT 的字串
     * @throws JOSEException 當 JWT 創建失敗時拋出
     */
	@GetMapping("/jwt")
	public ResponseEntity<String> getJWT(
			@RequestHeader("User-Agent") String userAgent,
			@RequestHeader("Service-Identifier") String serviceId,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws JOSEException {
		
		System.out.println(authorizationHeader);
		// 解碼 authorizationHeader
		String base64Credentials = authorizationHeader.substring("Basic ".length());
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Credentials);
        String decodedCredentials = new String(decodedBytes);
        String[] credentials = decodedCredentials.split(":", 2);
        String username = credentials[0];
        String password = credentials[1];
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        
        // 進行 username 與 password 的比對
        boolean loginCheck = users.containsKey(username) && users.get(username).equals(password); 
        if(!loginCheck) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        
        // 先判斷該使用者使否已經申請過 ?
        String storedJwt = tokens.get(username);
        if(storedJwt != null) {
        	// 驗證該 storedJwt 是否已經過期 ?
        	// 若已過期則重發
        	//storedJwt = jwtServiceNimbus.createToken(serviceId, username, 600_000);
        	return ResponseEntity.ok(storedJwt);
        }
        
        // 新建一個 token
		String userJWT = jwtServiceNimbus.createToken(serviceId, username, 600_000);
		// 加入到 tokens
		tokens.put(username, userJWT);
		return ResponseEntity.ok(userJWT);
	}
	
	
	
}
