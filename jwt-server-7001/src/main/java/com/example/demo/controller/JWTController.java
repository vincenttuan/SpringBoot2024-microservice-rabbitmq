package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.JWTServiceNimbus;
import com.nimbusds.jose.JOSEException;

//Access-Control-Allow-Origin
@CrossOrigin
@RestController
public class JWTController {
	
	@Autowired
	private JWTServiceNimbus jwtServiceNimbus;
	
	@GetMapping("/guestJWT")
	public ResponseEntity<String> getGuestJWT() throws JOSEException {
		String guestJWT = jwtServiceNimbus.createToken("guest", "user", 600_000);
		return ResponseEntity.ok(guestJWT);
	}
	
}
