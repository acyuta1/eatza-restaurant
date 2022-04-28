package com.eatza.restaurantsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = {"com.eatza.restaurantsearch", "com.eatza.shared.utils", "com.eatza.shared.security", "com.eatza.shared.config"})
@EnableEurekaClient
@EnableKafka
@EnableFeignClients(basePackages = "com.eatza.shared.feign")
public class RestaurantsearchserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantsearchserviceApplication.class, args);
	}

}
