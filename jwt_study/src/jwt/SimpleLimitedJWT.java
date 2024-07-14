package jwt;

import java.util.Date;

import com.nimbusds.jwt.JWTClaimsSet;

import util.KeyUtil;

/**
 * SimpleLimitedJWT 示例。
 * 
 * 這個類別展示如何創建具有有限時效性（即過期時間）的 JWT。當一個 JWT 有設定過期時間，
 * 它在該時間點後就不應再被接受或認為是有效的。
 * 
 * 主要流程如下：
 * 1. 生成一個簽名密鑰。
 * 2. 定義 JWT 的聲明 (claims)，其中包括令牌的主題、發行者、一些額外的資料，以及重要的過期時間。
 * 3. 使用密鑰對 JWT 進行簽名。
 * 4. 模擬讓令牌過期的情境（在這裡是等待 11 秒）。
 * 5. 嘗試驗證令牌的簽名以及檢查它是否已過期。
 * 
 * 這個範例展示了 JWT 的一個常見應用場景：給予令牌一定的生命週期，並在它過期後拒絕其存取權限。
 * 
 */
public class SimpleLimitedJWT {

	public static void main(String[] args) throws Exception {
		// 1. 生成一個簽名密鑰。
		String signingSecret = KeyUtil.generateSecret(32);	
		
		// 2. 定義 JWT 的聲明 (claims)，其中包括令牌的主題、發行者、一些額外的資料，以及重要的過期時間。
		long tenSecondsInMillis = 10_000; // 10秒
		Date expirationTime = new Date(new Date().getTime() + tenSecondsInMillis);
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject("user")
				.issuer("https://myapp.com")
				.claim("name", "john")
				.expirationTime(expirationTime) // 設定有效期限
				.build();
		
		// 3. 使用密鑰對 JWT 進行簽名。
		String token = KeyUtil.signJWT(claimsSet, signingSecret);
		System.out.println("token: " + token);
		
		// 4. 模擬讓令牌過期的情境（在這裡是等待 11 秒）。
		System.out.println("在這裡是等待 11 秒");
		Thread.sleep(11_000);
		
		// 5. 嘗試驗證令牌的簽名以及檢查它是否已過期。
		JWTClaimsSet claims = KeyUtil.getClaimsFromToken(token);
		
		if(KeyUtil.verifyJWTSignature(token, signingSecret)) {
			System.out.println("簽名驗證成功");
		} else {
			System.out.println("簽名驗證失敗");
		}
		
		// 檢查是否過期 ?
		// 若過期驗證 JWT 的簽名也會失效
		if(new Date().after(claims.getExpirationTime())) {
			System.out.println("令牌已將過期");
		} else {
			System.out.println("令牌未過期仍可以使用");
		}

	}

}

