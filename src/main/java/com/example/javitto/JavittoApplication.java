package com.example.javitto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class JavittoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavittoApplication.class, args);
	}

}
