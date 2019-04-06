package com.westernacher.internal.travelmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
		@PropertySource("classpath:/spring-boot.properties")
})
public class TravelManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelManagementApplication.class, args);
	}

}



