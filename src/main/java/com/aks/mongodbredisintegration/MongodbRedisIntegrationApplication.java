package com.aks.mongodbredisintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongodbRedisIntegrationApplication {

	public static void main(String[] args) {
		//https://github.com/rajadilipkolli/POC
		//SpringApplication.run(MongodbRedisIntegrationApplication.class, args);
		
		SpringApplication app = new SpringApplication(MongodbRedisIntegrationApplication.class);
		app.setWebApplicationType(WebApplicationType.REACTIVE);
		app.run(args);
	}

}
