package jwt;

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
