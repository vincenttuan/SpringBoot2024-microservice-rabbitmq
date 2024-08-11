# Spring Cloud Stream RabbitMQ

                                        +------------------+ 
                                        |                  | 
             +------------------------->+  Eureka-server   +<------------------------+  
             |                          |                  |                         |
             |                          +---------+--------+                         |
             |                                    |                                  |
             |                                    |                                  |
   +---------+--------+                 +---------+--------+               +---------+--------+
   |                  |                 |                  |               |                  |  
   |   starter-9901   |                 |  consumer-9902   +-- WebSocket --+   client-9903    |  
   |                  |                 |                  |      HTTP     |                  |
   +---------+--------+                 +---------+--------+               +------------------+
             |                                    |
             |     交換機: stock_direct_exchange    |
             |     隊列名: stock_queue              |
             |     路由鍵: tw.stock                 |
             |                                    |
   +---------+------------------------------------+--------+
   |                                                       |
   |                    RabbitMQ :5762                     |
   |                    Manager  :15672                    |
   |                                                       |
   +-------------------------------------------------------+

1. 
安裝 MySQL
建立 rabbitmq 資料庫

下載 https://drive.google.com/drive/folders/192hSs4zBnXVrEttBLOVOJ_8NYpxh6m3l?usp=drive_link
    下面的二個檔案
	price.json
	pre_price_0407.json
	MI_5MINS_0410_INDEX.csv
	
    並放到 C:\stock_price\ 中
   	

2. 啟動 rabbitmq-twstock-starter-9901 (Producer)
觀看 console 是否有報價（將 price.json 的資料發射到 RabbitMQ）
http://localhost:9901/stop <-- 停止報價
交換機: stock_direct_exchange (設定在 RabbitmqConfig.java --> new DirectExchange("stock_direct_exchange"))
發送時的 routing key: stock (設定在 PriceEmitterService.java --> rabbitTemplate.convertAndSend("stock_direct_exchange", "tw.stock", message))
orderNo.txt 會自動產生, 用來記錄目前最新下單編號作為建立新單時的參考

3. 啟動 rabbitmq-twstock-consumer-9902 (Consumer)
觀看 console 是否有報價（接收 RabbitMQ 的資料 ）
隊列名稱: stockQueue (設定在 StockPriceService.java --> @RabbitListener(queues = "stockQueue"))
收到之後會透過 WebSocket 傳送到前端
交換機: stock_direct_exchange
隊列: stock_queue
綁定的 routing key: tw.stock
上述三個設定在 RabbitmqConfig.java

4. 啟動 rabbitmq-twstock-client-9903 (Client)
只有 index.html 一個頁面，會透過 WebSocket 接收報價資料   
http://localhost:9903/index.html

