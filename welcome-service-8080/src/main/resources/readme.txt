

  +------------+   register   +-----------------+
  |   Eureka   | <----------- | welcome-service | 
  +------------+              +-----------------+
        ^
        |      register
        +-------------------- +---------------+ 
                              | hello-service |
                              +---------------+
將服務打包 jar 檔
1.
cd 到指定專案路徑下
例如: cd C:\SpringBoot2024-microservice-rabbitmq\welcome-service-8080

2.
打包指令 mvn clean package
            清除    打包 
            
透過 sts4/eclipse 來打包
welcome-service -> Run As -> Maven build -> Goals 輸入 "clean package" -> 按下 Run

3.
執行 jar
java -jar target/打包名稱.jar
例如: java -jar target/welcome-service-8080-0.0.1-SNAPSHOT.jar

java -jar C:\SpringBoot2024-microservice-rabbitmq\welcome-service-8080\target\welcome-service-8080-0.0.1-SNAPSHOT.jar

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
COPY target/welcome-service-8080-0.0.1-SNAPSHOT.jar /app/welcome-service.jar

# 容器啟動時執行的命令，啟動 Spring Boot 應用
CMD ["java", "-jar", "/app/welcome-service.jar"]

# 暴露 8080 端口，供外部訪問
EXPOSE 8080
--------------------------------------------------------------------------------
使用說明：
基礎鏡像：這裡使用的是 openjdk:17-jdk-slim，這是一個精簡版的 Java 17 JDK 官方鏡像，用於運行 Java 應用 （https://hub.docker.com/_/openjdk）。
工作目錄：設定容器內的工作目錄為 /app。
複製應用：將打包好的 Spring Boot 應用（jar 文件）從您的構建上下文（通常是您的項目根目錄，包含 Dockerfile 的位置）複製到容器的 /app 目錄下，並命名為 welcome-service.jar。
運行應用：容器啟動時，執行 java -jar /app/welcome-service.jar 命令啟動 Spring Boot 應用。
暴露端口：EXPOSE 8080 便於在運行容器時可以將此端口映射到主機。

** 注意請先要啟動 Docker Desktop **

2.構建 Docker 影像：
首先，確保您已經在專案根目錄下（包含 Dockerfile 的位置）成功執行了 Maven 打包命令 mvn clean package，並且 target 目錄中生成了 welcome-service-0.0.1-SNAPSHOT.jar 文件。
然後，在包含 Dockerfile 的目錄下運行以下命令來構建 Docker 鏡像：

docker build -t welcome-service . 
	-t 是 tag 的意思, 這裡是給這個鏡像取了一個名字叫 welcome-service
	注意最後有一個點, 表示當前目錄

查看鏡像
docker images 

刪除影像
docker rmi -f [映像檔名稱或ID]

刪除所有影像
docker rmi -f $(docker images -q)
docker images -q：這個命令會列出所有映像檔的 ID

3.運行 Docker 容器
構建完成後，運行以下命令來啟動容器：
docker run -p 8080:8080 --name my-welcome-container welcome-service 
				^    ^	 			  	// -p 是 port 將宿主機的 8080 端口映射到容器內的 8080 端口
                |    |                  // welcome-service 是所構建的鏡像名稱
                |    |                  // my-welcome-container 是容器名稱
              宿主機  容器內               // 8080:8080 是指 host:container
              這意味著 Docker 將宿主機的 8080 端口映射到容器內的 8080 端口，
              這樣宿主機上的應用程序就可以通過訪問 localhost:8080 來訪問容器內運行的應用程序。

================================================================================
所有容器:
docker stop $(docker ps -aq)
docker ps -aq 會列出所有容器的ID，不論其運行狀態。

刪除所有容器（包括已停止的）：注意有些是 k8s 的容器, 若 k82 執行中會刪不掉
docker rm $(docker ps -aq)

刪除所有 Pod
kubectl delete pods --all

刪除所有影像
docker rmi -f $(docker images -q)
docker images -q：這個命令會列出所有映像檔的 ID
