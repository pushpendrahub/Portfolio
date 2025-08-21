package com.portfolio.pushpendra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PushpendraApplication {

	public static void main(String[] args) {
		SpringApplication.run(PushpendraApplication.class, args);
	}

}

