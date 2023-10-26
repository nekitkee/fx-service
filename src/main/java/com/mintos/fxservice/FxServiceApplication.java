package com.mintos.fxservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FxServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxServiceApplication.class, args);
	}

}
