package jwt;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import util.KeyUtil;

/**
 * SimpleRevocationJWT 示範了 JWT 的撤銷機制。
 * 
 * 在許多應用場景中，僅僅擁有 JWT 的到期機制可能不足以滿足安全需求。有時，可能需要提前撤銷某個令牌。
 * 這個類別展示了一種可能的撤銷實現方式，即通過維護一個已撤銷令牌的集合來判斷 JWT 是否已被撤銷。
 * 
 * 主要流程如下：
 * 1. 生成 JWT。
 * 2. 撤銷 JWT。
 * 3. 驗證 JWT，並檢查其是否已被撤銷。
 */
public class SimpleRevocationJWT {
	
	// 儲存已撤銷的 JWT 黑名單
	private static Set<String> revokedTokens = new HashSet<>();
	
	public static void main(String[] args) throws JOSEException {
		String masterKey = KeyUtil.generateSecret(32); // 32 bytes 的密鑰長度
		JWTClaimsSet roomCardGenerator = new JWTClaimsSet.Builder()
				.subject("john") // 房客的身分
				.issuer("https://hotel.com") // 飯店發行單位
				.claim("roomNo", "101") // 自訂資訊: 房號
				//.claim("role", "管理者")
				.build(); // 建立房卡
		String signedRoomCard = KeyUtil.signJWT(roomCardGenerator, masterKey); // 將房卡簽名
		System.out.printf("房卡:%s%n", signedRoomCard);
		System.out.printf("房卡有效:%b%n", KeyUtil.verifyJWTSignature(signedRoomCard, masterKey));
		
		// 模擬 JWT 撤銷
		revokedTokens.add(signedRoomCard); // 將要撤銷的房卡放到黑名單集合中
		
		// 驗證 JWT 是否在黑名單中?
		if(KeyUtil.verifyJWTSignature(signedRoomCard, masterKey)) { // 驗證 JWT 是否有效
			// 驗證是否在黑名單中
			if(revokedTokens.contains(signedRoomCard)) {
				System.out.printf("房卡已在黑名單中:%s%n", signedRoomCard);
				System.out.println("房卡不可以使用...");
			} else {
				System.out.printf("房卡未在黑名單中:%s%n", signedRoomCard);
				System.out.println("房卡可以使用...");
			}
		} else {
			System.out.println("房卡失效或簽名驗證失敗");
		}
	}

}
