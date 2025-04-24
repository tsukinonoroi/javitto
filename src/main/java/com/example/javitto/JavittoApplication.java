package com.example.javitto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
public class JavittoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavittoApplication.class, args);
	}

}
