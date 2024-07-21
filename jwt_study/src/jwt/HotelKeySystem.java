package jwt;

import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import util.KeyUtil;

/**
 * HotelKeySystem 模擬一家酒店的房卡管理系統。
 * 在這個情境中，當房客入住酒店時，他們會得到一張「房間卡」，這張卡具有短暫的時效性，使他們能夠在有限的時間內進入房間。
 * 然而，這些房間卡有可能過期，特別是在長時間的住宿中。
 * 當房間卡過期時，房客可以到前台，前台擁有一台 Unified「房間卡產生器」，它可以製作新的房間卡。這台機器具有較長的時效性。
 * 這種模式類似於網路安全中的 Access Token 和 Refresh Token。房間卡相當於 Access Token，而房間卡產生器則相當於 Refresh Token。
 * 
 * 程式實作主要流程：
 * 1. 生成主要的機密鑰匙 (masterKey)。
 * 2. 創建並簽署「房間卡產生器」(Refresh Token)。
 * 3. 創建並簽署「房間卡」(Access Token)。
 * 4. 驗證「房間卡」是否過期。
 * 5. 若「房間卡」過期，使用「房間卡產生器」重新簽署新的「房間卡」。
 * 6. 模擬「房間卡產生器」過期後的情況。
 */
public class HotelKeySystem {
	
	private static String masterKey; // 金鑰
	
	// 創建並簽署「房間卡產生器」(Refresh Token)方法
	private static String createRoomCardGenerator() throws JOSEException {
		JWTClaimsSet roomCardGenerator = new JWTClaimsSet.Builder()
				.subject("FrontDesk") // 前台的身分
				.issuer("https://hotel.com") // 飯店發行單位
				.claim("authority", "create room card") // 自訂資訊
				.expirationTime(new Date(new Date().getTime() + 60_000)) // 設定房卡產生器的有效時間: 60秒
				.build(); // 建立房卡產生器
		String signedRoomCardGenerator = KeyUtil.signJWT(roomCardGenerator, masterKey); // 將房卡產生器簽名
		return signedRoomCardGenerator;
	}
	
	// 創建並簽署「房間卡」(Access Token)方法
	private static String createRoomCard(String guest, String roomNo) throws JOSEException {
		JWTClaimsSet roomCardGenerator = new JWTClaimsSet.Builder()
				.subject(guest) // 房客的身分
				.issuer("https://hotel.com") // 飯店發行單位
				.claim("roomNo", roomNo) // 自訂資訊: 房號
				.expirationTime(new Date(new Date().getTime() + 5_000)) // 設定房卡的有效時間: 5秒
				.build(); // 建立房卡
		String signedRoomCard = KeyUtil.signJWT(roomCardGenerator, masterKey); // 將房卡簽名
		return signedRoomCard;
	}
	
	public static void main(String[] args) throws JOSEException, InterruptedException {
		// 1. 生成主要的機密鑰匙 (masterKey)。
		masterKey = KeyUtil.generateSecret(32); // 32 bytes 的密鑰長度
		System.out.printf("機密鑰匙:%s%n", masterKey);
		
		// 2. 創建並簽署「房間卡產生器」(Refresh Token)。
		String signedRoomCardGenerator = createRoomCardGenerator();
		System.out.printf("房間卡產生器(Refresh Token):%s%n", signedRoomCardGenerator);
		
		// 3. 創建並簽署「房間卡」(Access Token)。
		String signedRoomCard = createRoomCard("John", "101");
		System.out.printf("房間卡(Access Token):%s%n", signedRoomCard);
		
		// 4. 驗證「房間卡」是否過期。
		while (true) {
			// 用房間卡開門, 判斷房間卡是否失效
			boolean isRoomCardExpired = !KeyUtil.verifyJWTSignature(signedRoomCard, masterKey);
			System.out.printf("用房間卡開門, 房間卡是否失效: %b%n", isRoomCardExpired);
			if(isRoomCardExpired) {
				System.out.println("房卡無效請到前台重新辦理");
				break;
			}
			System.out.println("房卡有效開門進入");
			Thread.sleep(1000);
		}
		
		// 5. 若「房間卡」過期，使用「房間卡產生器」重新簽署新的「房間卡」。
		signedRoomCard = createRoomCard("john", "101");
		System.out.printf("重發房間卡(Access Token):%s%n", signedRoomCard);
		boolean isRoomCardNonExpired = KeyUtil.verifyJWTSignature(signedRoomCard, masterKey);
		System.out.printf("重發的房間卡是否有效: %b%n", isRoomCardNonExpired);
		
		// 6. 模擬「房間卡產生器」過期後的情況。
		Thread.sleep(60_000);
		
		// 檢查房間卡產生器是否過期
		if(!KeyUtil.verifyJWTSignature(signedRoomCardGenerator, masterKey)) {
			System.out.println("房間卡產生器已過期");
			signedRoomCardGenerator = createRoomCardGenerator(); // 重發房間卡產生器
			System.out.printf("重發房間卡產生器(Refresh Token):%s%n", signedRoomCardGenerator);
			boolean isRoomCardGeneratorNonExpired = KeyUtil.verifyJWTSignature(signedRoomCardGenerator, masterKey);
			System.out.printf("重發的房間卡產生器是否有效: %b%n", isRoomCardGeneratorNonExpired);
		}
	}
	
}
