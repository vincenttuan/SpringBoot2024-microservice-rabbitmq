package com.example.demo;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class 取得歷史股票交易紀錄 {
	
//	public static void main(String[] args) throws Exception {
//		String[] days = {"07", "10"};
//		for (String day : days) {
//	        submit(day);
//	    }
//	}
	
	public static void submit(String day) throws Exception {
		System.out.println("取得 112/04/" + day + " 所有股票交易紀錄");
		
		String jsonFileFolder = null;
    	// 判斷作業系統是否是 Windows
    	if (System.getProperty("os.name").toLowerCase().contains("windows")) {
    		jsonFileFolder = "C:/Users/vince/Downloads";
    	} else {
    		// mac os
    		jsonFileFolder = "/Volumes/SSD/data";
    	}
    	
    	String jsonFilePath = jsonFileFolder + "/symbols.txt";
    	System.out.println("=> " + jsonFilePath);
    	
    	List<String> symbols = Files.readAllLines(Paths.get(jsonFilePath));
    	
    	jsonFileFolder = null;
    	// 判斷作業系統是否是 Windows
    	if (System.getProperty("os.name").toLowerCase().contains("windows")) {
    		jsonFileFolder = "C:/Users/vince/Downloads";
    	} else {
    		// mac os
    		jsonFileFolder = "/Volumes/SSD/data";
    	}
    	
    	jsonFilePath = jsonFileFolder + "/pre_price_04" + day + ".json";
    	
    	// 若檔案存在就刪除檔案
    	if (Files.exists(Paths.get(jsonFilePath))) {
    		Files.delete(Paths.get(jsonFilePath));
    	}
    	
    	// 建立檔案
    	Files.createFile(Paths.get(jsonFilePath));
    	
		for (String symbol : symbols) {
			symbol = symbol.replace("\"", "");
			System.out.println("=> " +symbol);
			write(jsonFilePath, symbol, day);
			Thread.sleep(5000);
		}
    	
	}
	
	public static void write(String jsonFilePath, String symbol, String day) throws IOException {
		
    	
		/*
		 * URL: https://www.twse.com.tw/pcversion/zh/exchangeReport/STOCK_DAY
		 * Mathod: Post
		 * Form Data:
		 *         response: json
		 *         date: 20230401
		 *         stockNo: 2330
		 * */
		String url = "https://www.twse.com.tw/pcversion/zh/exchangeReport/STOCK_DAY";
		String response = "json";
		String date = "202304" + day;
		String stockNo = symbol;
		// 利用 Java 11 HttpClient
		// 取得 2023/04/01 2330 股票交易紀錄
		// 並將結果印出
		// URL和请求参数
        Map<String, String> arguments = new HashMap<>();
        arguments.put("response", response);
        arguments.put("date", date);
        arguments.put("stockNo", stockNo);
        
        // 创建HttpClient
        HttpClient client = HttpClient.newHttpClient();
        
        // 构建请求正文
        StringJoiner sj = new StringJoiner("&");
        for(Map.Entry<String,String> entry : arguments.entrySet())
            sj.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" 
                     + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        byte[] requestData = sj.toString().getBytes(StandardCharsets.UTF_8);

        // 创建POST请求
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofByteArray(requestData))
            .build();
        
        try {
            // 发送请求并获取响应
            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            // 打印响应正文
            //System.out.println(resp.body());
            JsonObject jsonResponse = JsonParser.parseString(resp.body()).getAsJsonObject();

            // 獲取數據部分
            JsonArray data = jsonResponse.getAsJsonArray("data");
            // 獲取標題部分
            // 範例："title": "112年04月 2330 台積電           各日成交資訊"
            String title = jsonResponse.get("title").toString();
            // 取得股票名稱
            String stockName = title.split(" ")[2];
            // 遍歷數據尋找特定日期
            for (JsonElement element : data) {
                JsonArray record = element.getAsJsonArray();
                String targetDate = record.get(0).getAsString(); // 獲取日期字段

                if (("112/04/" + day).equals(targetDate)) {
                    // 使用新方法将股票代号加到记录的开头
                    JsonArray updatedRecord = addStockNoFirst(record, stockNo, stockName);

                    // 将更新后的记录转换为JsonObject
                    JsonObject recordJson = recordToJson(updatedRecord);

                    // 打印JsonObject
                    System.out.println(recordJson);
                    
                    // 寫入到 pre_price.json 檔案中, 在原本後面加上一行, 若檔案不存在則建立
                    Path path = Paths.get(jsonFilePath);

	            	// 检查文件是否存在，如果不存在则先创建文件
                    if (!Files.exists(path)) {
                    	// 确保父目录存在
                    	Path parentDir = path.getParent();
                    	if (!Files.exists(parentDir)) {
                    		Files.createDirectories(parentDir);
                    	}
                    	Files.createFile(path);
                    }
                    
                    String recordJsonStr = recordJson.toString();
                    
                    Files.writeString(Paths.get(jsonFilePath), recordJsonStr + "," + "\n", StandardOpenOption.APPEND);
                    System.out.println(stockNo + " 已寫入到 " + jsonFilePath + " 檔案中");
                    
                    break; // 找到后跳出循环
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	
	}
	
	private static JsonObject recordToJson(JsonArray record) {
		// 代號, 名稱, 日期, 成交股數, 成交金額, 開盤價, 最高價, 最低價, 收盤價, 漲跌價差, 成交筆數
	    String[] titlesEnglish = {"symbol", "name", "date", "volume", "amount", "openPrice", "highPrice", "lowPrice", "closePrice", "priceChange", "transaction"};
	    JsonObject result = new JsonObject();

	    // 假设record的长度和titlesEnglish的长度一致
	    for (int i = 0; i < titlesEnglish.length; i++) {
	        // 将每个字段添加到JsonObject中
	        result.addProperty(titlesEnglish[i], record.get(i).getAsString());
	    }

	    return result;
	}
	
	// 在指定的记录中加入股票代號作為第一個元素
	private static JsonArray addStockNoFirst(JsonArray originalRecord, String stockNo, String stockName) {
	    JsonArray newRecord = new JsonArray();
	    
	    // 將股票代號與股票名稱加入
	    newRecord.add(stockNo);
	    newRecord.add(stockName);
	    
	    // 將原始數據的其餘部分添加到新數組中
	    for (JsonElement element : originalRecord) {
	        newRecord.add(element);
	    }
	    
	    return newRecord;
	}
}
