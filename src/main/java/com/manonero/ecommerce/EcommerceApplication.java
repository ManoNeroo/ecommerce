package com.manonero.ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.manonero.ecommerce.models.StorageProperties;
import com.manonero.ecommerce.services.IStorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	CommandLineRunner initStorage(IStorageService storageService) {
		return (args) -> {
			// storageService.deleteAll();
			storageService.init();
		};
	}

}
