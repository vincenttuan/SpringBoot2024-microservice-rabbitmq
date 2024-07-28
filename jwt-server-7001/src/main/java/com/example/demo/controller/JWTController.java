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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.JWTServiceNimbus;
import com.nimbusds.jose.JOSEException;

import jakarta.servlet.http.HttpServletRequest;

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
        
        // 新建一個 token(有效期 600 秒)
		String userJWT = jwtServiceNimbus.createToken(serviceId, username, 600_000);
		// 加入到 tokens
		tokens.put(username, userJWT);
		return ResponseEntity.ok(userJWT);
	}
	
	/**
     * 清除與指定用戶名相關聯的 token。
     * <p>
     * 此方法接受作為請求參數提供的 JWT token，或從 Authorization Header 中獲取 JWT token，
     * 解析出其中的用戶名，並從 tokens Map 中移除該用戶名相關聯的 token。
     * 如果成功清除，返回 200 OK 和消息。
     * 如果未找到用戶名相關聯的 token，返回 401 Unauthorized。
     *
     * @param token      可選的請求參數中的 JWT
     * @param authHeader 可選的 HTTP Header 中的 JWT
     * @return 返回清除結果的消息或 401 Unauthorized 狀態
     */
	@GetMapping("/clear")
	public ResponseEntity<String> clear(
			@RequestParam(required = false) String token,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		
		// 如果請求參數中未提供 token, 則嘗試從 Header 中獲取
		if(token == null || token.isEmpty()) {
			// 檢查 Authorization Header 是否存在且以 "Bearer " 開頭
			if(authHeader != null && authHeader.startsWith("Bearer ")) {
				// 移除 "Bearer " 前綴, 來得到真正的 token 字串
				token = authHeader.substring("Bearer ".length());
			}
		}
		
		// 如果二者都未提供, 返回 400
		if(token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("缺少 Token");
		}
		
		// 從 JWT 中提取用戶名
		String username = jwtServiceNimbus.getJWTClaims(token).get("username").toString();
		System.out.println("清除 tokens 中的 " + username);
		
		// 從 tokens Map 中移除與用戶名稱相關聯的 token
		if(tokens.remove(username) == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(username + " 的 token 找不到");
		}
		
		return ResponseEntity.ok(username + " 的 token 已被清除");
	}
	
	/**
     * 驗證 JWT 的有效性。
     * 可以通過兩種方式提供 token：
     * 1. 作為請求參數提供，如：/verifyJwt?token=your_jwt_token
     * 2. 作為 HTTP Header 提供，Header 名稱為 Authorization，格式為：Bearer your_jwt_token
     * 如果兩者都未提供，則返回 400 Bad Request 狀態碼和錯誤消息。
     *
     * @param token      可選的請求參數中的 JWT
     * @param authHeader 可選的 HTTP Header 中的 JWT
     * @param request    HttpServletRequest 對象，用於獲取請求的 IP 和端口
     * @return 返回 JWT 的驗證結果
     */
	@GetMapping("/verifyJwt")
	public ResponseEntity<String> verifyJwt(
			@RequestParam(required = false) String token,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
			HttpServletRequest request) {
		
		// 如果請求參數中未提供 token, 則嘗試從 Header 中獲取
		if(token == null || token.isEmpty()) {
			// 檢查 Authorization Header 是否存在且以 "Bearer " 開頭
			if(authHeader != null && authHeader.startsWith("Bearer ")) {
				// 移除 "Bearer " 前綴, 來得到真正的 token 字串
				token = authHeader.substring("Bearer ".length());
			}
		}
		
		// 如果二者都未提供, 返回 400
		if(token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("缺少 Token");
		}
		
		// 來自 ip
		String ip = request.getRemoteAddr();
		int port = request.getRemotePort();
		System.out.println("來自: " + ip + ":" + port + " 的請求");
		
		// 驗證 JWT 是否有效
		System.out.println("驗證 token: " + token);
		if(!jwtServiceNimbus.verifyToken(token)) {
			System.out.println("驗證 token 失敗, JWT 是無效的: " + token);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 是無效的");
		}
		
		// 取得用戶
		String username = jwtServiceNimbus.getJWTClaims(token).get("username").toString();
		
		// 檢查用戶名稱是否存在於 tokens map 中
		if(!tokens.containsKey(username)) {
			System.out.println("JWT 是有效的. 但是用戶名不存在於 tokens map 中");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 是有效的. 但是用戶名不存在於 tokens map 中");
		}
		
		System.out.println("JWT 是有效的");
		return ResponseEntity.ok("JWT 是有效的");
	}
	
	
	
	
}
