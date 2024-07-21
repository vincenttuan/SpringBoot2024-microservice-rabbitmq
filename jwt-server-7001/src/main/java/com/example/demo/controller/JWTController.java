package com.example.demo.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	// 申請一個訪客 jwt
	@GetMapping("/guestJWT")
	public ResponseEntity<String> getGuestJWT() throws JOSEException {
		String guestJWT = jwtServiceNimbus.createToken("guest", "user", 600_000);
		return ResponseEntity.ok(guestJWT);
	}
	
	// 申請一個正式 jwt
	// Header: User-Agent: 使用者名字(Ex:john)
	//         Service-Identifier: 服務 id(Ex:report)
	//         Authorization: Basic 使用者帳密
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
        
		String userJWT = jwtServiceNimbus.createToken(serviceId, username, 600_000);
		
		return ResponseEntity.ok(userJWT);
	}
	
}
