package jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import util.KeyUtil;

public class SimpleJWTJWE {
	
	public static void main(String[] args) throws Exception {
		// 1. 生成簽名密鑰與資料加密密鑰 (JWK)
		String signingSecret = KeyUtil.generateSecret(32); // 用於簽名
		System.out.println("簽名密鑰:");
		System.out.println(signingSecret);
		String encryptionSecret = KeyUtil.generateSecret(16); // 用於資料加密
		System.out.println("資料加密密鑰:");
		System.out.println(encryptionSecret);
		
		// 2. 創建 JWT 的聲明 (chaims/payload)
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject("user") // 主題: 一個用戶
				.issuer("http://myapp.com") // 發行者
				.claim("name", "John") // 自訂聲明: 使用者姓名
				.claim("birth", "2000-07-14") // 自訂聲明: 使用者生日
				.claim("light", "On") // 自訂聲明: 開燈
				.build();
		
		// 3. 使用 JWS 來對 JWT 進行簽名
		SignedJWT signedJWT = KeyUtil.signWithSecret(claimsSet, signingSecret); 
		
		// 4. 使用 JWE 來加密 JWT
		String encryptedJWT = KeyUtil.encryptJWT(signedJWT, encryptionSecret);
		
		// 輸出 JWT token
		System.out.println("已簽名並加密的 JWT Token:");
		System.out.println(encryptedJWT);
		
		// 5. 解密 JWT Token
		SignedJWT decryptedSignedJWT = KeyUtil.decryptJWT(encryptedJWT, encryptionSecret);
		
		// 4. 驗證已解密 JWT Token 簽名
		if(decryptedSignedJWT.verify(new MACVerifier(signingSecret))) {
			System.out.println("Token 簽名驗證成功");
			// 將 claims/payload DATA 取出
			JWTClaimsSet claims = decryptedSignedJWT.getJWTClaimsSet();
			System.out.printf("主題 subject: %s%n", claims.getSubject());
			System.out.printf("發行 issuer: %s%n", claims.getIssuer());
			System.out.printf("自訂-使用者姓名 name: %s%n", claims.getStringClaim("name"));
			System.out.printf("自訂-使用者生日 birth: %s%n", claims.getStringClaim("birth"));
			System.out.printf("自訂-開燈 light: %s%n", claims.getStringClaim("light"));
		} else {
			System.out.println("Token 簽名驗證失敗");
		}
		
	}
}
