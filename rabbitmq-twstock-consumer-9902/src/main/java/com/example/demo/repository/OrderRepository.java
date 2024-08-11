package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.po.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	@Query(value = "SELECT "
			+ "    symbol AS symbol, "
			+ "    SUM(match_price * match_amount) / SUM(match_amount) AS avgPrice, "
			+ "    SUM(match_amount) AS amount "
			+ "FROM "
			+ "    orders "
			+ "WHERE "
			+ "    status = 'true' and bs = 'B' "
			+ "GROUP BY symbol", nativeQuery = true)
	public List<Object[]> findAllOrderDto();
	
	// 根據 symbol 查詢
	public List<Order> findBySymbol(String symbol);
	
	// 根據 id 查詢, 只能刪除一筆
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM orders WHERE symbol = ?1 LIMIT 1", nativeQuery = true)
	public int deleteSingleOrderBySymbol(String symbol);

	
	
}
