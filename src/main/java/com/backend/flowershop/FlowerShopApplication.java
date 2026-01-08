package com.backend.flowershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FlowerShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowerShopApplication.class, args);
	}

}
