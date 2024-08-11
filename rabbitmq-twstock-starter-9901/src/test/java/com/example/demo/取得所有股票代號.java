package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class 取得所有股票代號 {

//	public static void main(String[] args) throws IOException {
//		// 取得所有股票代號並存放在 Set<String> 物件中, 最後寫入到 symbols.txt 檔案中
//		Set<String> symbols = new HashSet<>();
//		
//		String jsonFileFolder = null;
//    	// 判斷作業系統是否是 Windows
//    	if (System.getProperty("os.name").toLowerCase().contains("windows")) {
//    		jsonFileFolder = "C:/Users/vince/Downloads";
//    	} else {
//    		// mac os
//    		jsonFileFolder = "/Volumes/SSD/data";
//    	}
//    	
//    	String jsonFilePath = jsonFileFolder + "/price.json";
//    	
//        String jsonStr = Files.readString(Paths.get(jsonFilePath));
//        String[] jsonStrs = jsonStr.split("\n");
//
//        for (String json : jsonStrs) {
//        	String message = json.trim();
//            if (message.length() < 10 || message.contains("\"symbol\":\"000000\"")) {
//                continue;
//            }
//            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
//            String symbol = jsonObject.get("symbol") + "";
//            symbols.add(symbol);
//        }
//        
//        Files.write(Paths.get(jsonFileFolder + "/symbols.txt"), symbols);
//        System.out.println("symbols.txt 已產生");
//	}

}
