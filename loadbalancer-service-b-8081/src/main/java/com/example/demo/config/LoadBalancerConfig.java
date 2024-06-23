package com.example.demo.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;

// 自行配置附載平衡策略
@Configuration
@LoadBalancerClients({
	//@LoadBalancerClient(name = "loadbalancer-service-c-9001", configuration = RandomLoadBalancerConfig.class),
	@LoadBalancerClient(name = "loadbalancer-service-c-9001", configuration = RoundRobinLoadBalancerConfig.class)
})
public class LoadBalancerConfig {
	@Bean
	@LoadBalanced
	public Feign.Builder feignBuilder() {
		return Feign.builder();
	}
}

// 隨機分配器
class RandomLoadBalancerConfig {
	@Bean
	public RandomLoadBalancer randomLoadBalancer(LoadBalancerClientFactory clientFactory) {
		return new RandomLoadBalancer(clientFactory.getLazyProvider("loadbalancer-service-c-9001", ServiceInstanceListSupplier.class), "loadbalancer-service-c-9001");
	}
}

// 輪詢分配器
class RoundRobinLoadBalancerConfig {
	@Bean
	public RoundRobinLoadBalancer roundRobinLoadBalancer(LoadBalancerClientFactory clientFactory) {
		return new RoundRobinLoadBalancer(clientFactory.getLazyProvider("loadbalancer-service-c-9001", ServiceInstanceListSupplier.class), "loadbalancer-service-c-9001");
	}
}

