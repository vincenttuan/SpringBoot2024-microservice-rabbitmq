

  +------------+   register   +-----------------------+
  |   Eureka   | <----------- | feign-product-service | 
  +------------+              +-----------------------+

將服務打包 jar 檔
1.
cd 到指定專案路徑下
例如: cd C:\SpringBoot2024-microservice-rabbitmq\feign-product-service-9091

2.
打包指令 mvn clean package
            清除    打包 
            
透過 sts4/eclipse 來打包
feign-product-service-9091 -> Run As -> Maven build -> Goals 輸入 "clean package" -> 按下 Run

3.
執行 jar
java -jar target/打包名稱.jar
例如: java -jar target/feign-product-service-9091-0.0.1-SNAPSHOT.jar

java -jar C:\SpringBoot2024-microservice-rabbitmq\feign-product-service-9091\target\feign-product-service-9091-0.0.1-SNAPSHOT.jar

停止服務
Ctrl + C

================================================================================
Docker
1. 撰寫 Dockerfile
以下是一個基本的 Dockerfile 來封裝您的 Spring Boot 應用 (welcome-service-8080) 為 Docker 容器：

Dockerfile
--------------------------------------------------------------------------------
# 使用官方 Java 運行環境作為基礎鏡像
FROM openjdk:17-jdk-slim

# 設定容器內部工作目錄
WORKDIR /app

# 將應用的 jar 文件從構建上下文複製到容器內
COPY target/product-service-9091-0.0.1-SNAPSHOT.jar /app/product-service.jar

# 容器啟動時執行的命令，啟動 Spring Boot 應用
CMD ["java", "-jar", "/app/product-service.jar"]

# 暴露 9091 端口，供外部訪問
EXPOSE 9091
--------------------------------------------------------------------------------

** 注意請先要啟動 Docker Desktop **

2.構建 Docker 影像：
docker build -t product-service . 

查看鏡像
docker images 

3.運行 Docker 容器
構建完成後，運行以下命令來啟動容器：
docker run -p 9091:9091 --name my-product-container product-service 
