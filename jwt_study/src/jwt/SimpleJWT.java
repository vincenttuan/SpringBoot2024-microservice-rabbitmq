package jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;

import util.KeyUtil;

public class SimpleJWT {
	
	public static void main(String[] args) throws Exception {
		// 1. 生成簽名密鑰 (JWK)
		String signingSecret = KeyUtil.generateSecret(32);
		System.out.println("簽名密鑰:");
		System.out.println(signingSecret);
		
		// 2. 創建 JWT 的聲明 (chaims/payload)
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject("user") // 主題: 一個用戶
				.issuer("http://myapp.com") // 發行者
				.claim("name", "John") // 自訂聲明: 使用者姓名
				.claim("birth", "2000-07-14") // 自訂聲明: 使用者生日
				.claim("light", "On") // 自訂聲明: 開燈
				.build();
		
		// 3. 進行簽名並得到 Token
		// JWS, JWA(HS256)
		String token = KeyUtil.signJWT(claimsSet, signingSecret);
		
		// 輸出 token
		System.out.println("已簽名但是 payload 無加密的 JWT Token:");
		System.out.println(token);
		
		// 4. 驗證 Token 簽名
		if(KeyUtil.verifyJWTSignature(token, signingSecret)) {
			System.out.println("Token 簽名驗證成功");
			// 將 claims/payload DATA 取出
			JWTClaimsSet claims = KeyUtil.getClaimsFromToken(token);
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
