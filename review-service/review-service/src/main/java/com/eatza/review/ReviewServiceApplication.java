package com.eatza.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.eatza.review", "com.eatza.shared.utils", "com.eatza.shared.security", "com.eatza.shared.config"})
//@SpringBootApplication(scanBasePackages = {"com.eatza.review", "com.eatza.shared"})
@EnableFeignClients(basePackages = "com.eatza.shared.feign")
@EnableEurekaClient
public class ReviewServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReviewServiceApplication.class, args);
	}

}
