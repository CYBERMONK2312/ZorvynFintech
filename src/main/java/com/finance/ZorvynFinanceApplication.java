package com.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZorvynFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZorvynFinanceApplication.class, args);
	}

}
